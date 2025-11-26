package com.example.demo.application.service;

import com.example.demo.application.dto.UploadFileCommand;
import com.example.demo.application.dto.commands.RegistrarInmobiliariaCommand;
import com.example.demo.application.dto.queries.InmobiliariaDashBoardDto;
import com.example.demo.application.exceptions.EntidadDuplicadaException;
import com.example.demo.application.exceptions.ReglasNegocioException;
import com.example.demo.application.interfaces.external.IStorageService;
import com.example.demo.application.services.CrearInmobiliariaServiceImpl;
import com.example.demo.domain.model.Inmobiliarias;
import com.example.demo.domain.repository.InmobilariaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CrearInmobiliariaServiceImplTest {

    @Mock // Simulamos el Repositorio (BD)
    private InmobilariaRepository inmobiliariaRepository;

    @Mock // Simulamos el Storage (S3)
    private IStorageService storageService;

    @InjectMocks // Probamos el Servicio Real con los mocks inyectados
    private CrearInmobiliariaServiceImpl service;

    // Helper para crear un comando válido rápido
    private RegistrarInmobiliariaCommand crearComandoValido() {
        InputStream stream = new ByteArrayInputStream("fake-image".getBytes());
        UploadFileCommand logo = new UploadFileCommand("logo.png", "image/png", 100L, stream);
        return new RegistrarInmobiliariaCommand(
                "20123456789", "Inmo Test SAC", List.of("Proyecto A", "Proyecto B"), logo
        );
    }

    // -------------------------------------------------------------------
    // CASO 1: ÉXITO (HAPPY PATH) - SIN CAMBIOS
    // -------------------------------------------------------------------
    @Test
    @DisplayName("Debe crear inmobiliaria, subir logo y guardar proyectos correctamente")
    void crear_Exito() {
        RegistrarInmobiliariaCommand command = crearComandoValido();

        when(storageService.subirArchivo(any(), anyString())).thenReturn("http://s3/logo.png");
        when(inmobiliariaRepository.guardarInmobiliaria(any(Inmobiliarias.class))).thenReturn(50L);

        InmobiliariaDashBoardDto resultado = service.crearInmobiliaria(command, "ADMIN");

        assertNotNull(resultado);
        assertEquals(50L, resultado.idImobiliara());
        verify(inmobiliariaRepository, times(2)).guardarProyectos(any(), eq(50L));
    }

    // -------------------------------------------------------------------
    // CASO 2: ERROR DE VALIDACIÓN (DOMINIO -> REGLAS NEGOCIO)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("Debe lanzar ReglasNegocioException si el RUC es inválido")
    void crear_RucInvalido() {
        RegistrarInmobiliariaCommand command = new RegistrarInmobiliariaCommand(
                "123ABC45678", "Nombre", null, null
        );

        assertThrows(ReglasNegocioException.class, () -> {
            service.crearInmobiliaria(command, "ADMIN");
        });

        verify(inmobiliariaRepository, never()).guardarInmobiliaria(any());
    }

    // -------------------------------------------------------------------
    // CASO 3: DUPLICADO EN BD (SIMULACIÓN CORRECTA)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("Debe relanzar EntidadDuplicadaException si el repositorio la lanza")
    void crear_Duplicado() {
        RegistrarInmobiliariaCommand command = new RegistrarInmobiliariaCommand(
                "20123456789", "Nombre", null, null
        );


        when(inmobiliariaRepository.guardarInmobiliaria(any()))
                .thenThrow(new EntidadDuplicadaException("El RUC ya existe (Simulado)"));


        Exception ex = assertThrows(EntidadDuplicadaException.class, () -> {
            service.crearInmobiliaria(command, "ADMIN");
        });

        assertEquals("El RUC ya existe (Simulado)", ex.getMessage());
    }


}
