package com.example.demo.api.controller;

import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.commands.RegistrarAsesorCommand;
import com.example.demo.application.dto.queries.AsesorDashBoardDto;
import com.example.demo.application.exceptions.RecursoNoEncontradoException;
import com.example.demo.application.interfaces.usecases.ListarAsesorService;
import com.example.demo.application.interfaces.usecases.ListarAsesoresExternosService;
import com.example.demo.application.interfaces.usecases.RegistrarAsesorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser; // Necesario para saltar seguridad
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf; // Solo si tienes seguridad activa
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AsesorControllerTest {


    @Autowired
    private MockMvc mockMvc; // Simula las peticiones HTTP

    @Autowired
    private ObjectMapper objectMapper; // Para convertir Objetos <-> JSON

    // Mockeamos los 3 Servicios que usa el controlador
    @MockitoBean private ListarAsesoresExternosService listarExternosService;
    @MockitoBean private ListarAsesorService listarInternosService;
    @MockitoBean private RegistrarAsesorService registrarService;

    // -------------------------------------------------------------------
    // TEST 1: LISTAR INTERNOS (Happy Path)
    // -------------------------------------------------------------------
    @Test
    @WithMockUser(authorities = "SCOPE_ADMIN")
    @DisplayName("GET /asesores (Interno) -> Debe retornar 200 y lista paginada")
    void listarInternos_Exito() throws Exception {

        // 1. GIVEN (Preparamos la respuesta simulada del servicio)
        AsesorDashBoardDto dto = new AsesorDashBoardDto("A1", "Juan Perez", "Ica", "Hipo");
        PaginationResponseDTO<AsesorDashBoardDto> respuestaService = new PaginationResponseDTO<>(
                List.of(dto), 1, 10, 1, 1, true
        );

        when(listarInternosService.listarAsesoresGestion(anyInt(), anyInt(), any()))
                .thenReturn(respuestaService);

        // 2. WHEN & THEN (Ejecutamos y Verificamos)
        mockMvc.perform(get("/api/v1/asesores")
                        .param("origen", "INTERNO")
                        .param("nombre", "Juan")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Esperamos 200 OK
                .andExpect(jsonPath("$.content[0].nombreCompleto").value("Juan Perez")) // Verificamos JSON
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    // -------------------------------------------------------------------
    // TEST 2: REGISTRAR (Happy Path)
    // -------------------------------------------------------------------
    @Test
    @WithMockUser(authorities = "SCOPE_ADMIN") // Si tu endpoint requiere rol ADMIN
    @DisplayName("POST /asesores -> Debe retornar 201 Created")
    void registrar_Exito() throws Exception {

        // 1. GIVEN
        RegistrarAsesorCommand command = new RegistrarAsesorCommand("A1", "Juan", "Perez", "c@c.com", "Ica");
        AsesorDashBoardDto respuesta = new AsesorDashBoardDto("A1", "Juan Perez", "Ica", "Hipo");

        when(registrarService.registrarAsesor(any(RegistrarAsesorCommand.class)))
                .thenReturn(respuesta);

        // 2. WHEN & THEN
        mockMvc.perform(post("/api/v1/asesores")
                        .with(csrf()) // Token CSRF necesario en tests POST con seguridad
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command))) // Convertimos objeto a JSON string
                .andExpect(status().isCreated()) // Esperamos 201
                .andExpect(jsonPath("$.idAsesor").value("A1"));
    }

    // -------------------------------------------------------------------
    // TEST 3: MANEJO DE ERRORES (GlobalExceptionHandler)
    // -------------------------------------------------------------------
    @Test
    @WithMockUser(authorities = "SCOPE_ADMIN")
    @DisplayName("POST /asesores -> Debe retornar 404 si la ciudad no existe")
    void registrar_ErrorCiudadNoExiste() throws Exception {

        // 1. GIVEN
        RegistrarAsesorCommand command = new RegistrarAsesorCommand("A1", "Juan", "Perez", "c@c.com", "Narnia");

        // Simulamos que el servicio lanza la excepción de negocio
        when(registrarService.registrarAsesor(any()))
                .thenThrow(new RecursoNoEncontradoException("Ciudad Narnia no encontrada"));

        // 2. WHEN & THEN
        mockMvc.perform(post("/api/v1/asesores")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isNotFound()) // ¡Esperamos 404! (Gracias al Advice)
                .andExpect(jsonPath("$.codigo").value("404"))
                .andExpect(jsonPath("$.mensaje").value("Ciudad Narnia no encontrada"));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_USER")
    @DisplayName("POST /asesores -> Debe retornar 403 Forbidden si NO tiene rol ADMIN")
    void registrar_SinPermisos_DeberiaFallar() throws Exception {

        // 1. GIVEN
        RegistrarAsesorCommand command = new RegistrarAsesorCommand("A1", "Hacker", "Malo", "x@x.com", "Ica");

        // 2. WHEN & THEN
        // No necesitamos mockear el servicio porque Spring Security debería bloquearlo ANTES de llegar al servicio.
        mockMvc.perform(post("/api/v1/asesores")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isForbidden()); // Esperamos el 403
    }
}
