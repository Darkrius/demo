package com.example.demo.infraestructure.repository;
import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.InmobiliariaDashBoardDto;
import com.example.demo.domain.model.Inmobiliarias;
import com.example.demo.domain.model.Proyectos;
import com.example.demo.domain.repository.InmobilariaRepository;
import com.example.demo.infraestructure.config.LegacyDataSourceConfig;
import com.example.demo.infraestructure.config.PrincipalDataSourceConfig;
import com.example.demo.infraestructure.persistence.repositorys.InmobiliariaDashServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import java.security.SecureRandom;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Tag("integration")
@Transactional // <--- VITAL: Borra los datos al terminar el test
@Import({PrincipalDataSourceConfig.class, LegacyDataSourceConfig.class})
class InmobiliariaDashServiceImplTest {

    @Autowired
    private InmobiliariaDashServiceImpl dashboardRepository; // El que vamos a probar (Lectura)

    @Autowired
    private InmobilariaRepository commandRepository; // Helper para insertar datos (Escritura)

    // Helper para RUC único
    private final SecureRandom secureRandom = new SecureRandom();

    private String generarRucValido() {
        // Genera un número entero entre 0 y 899,999,999 y le suma 100,000,000
        // Resultado: Un número de 9 dígitos (100000000 a 999999999)
        int aleatorio = 100_000_000 + secureRandom.nextInt(900_000_000);

        return "20" + aleatorio;
    }

    @Test
    @DisplayName("Debe listar el dashboard con el conteo de proyectos correcto")
    void listarDashboard_Exito() {
        // 1. GIVEN (Preparamos el escenario en BD)
        String adminId = "ADMIN_TESTER";
        String ruc = generarRucValido();
        String razonSocial = "Inmobiliaria Dashboard Test SAC";

        // A. Creamos la Inmobiliaria
        Inmobiliarias padre = Inmobiliarias.crear(ruc, razonSocial, adminId, null);
        long idPadre = commandRepository.guardarInmobiliaria(padre);

        // B. Le agregamos 2 proyectos
        commandRepository.guardarProyectos(Proyectos.crear("Proyecto Alpha"), idPadre);
        commandRepository.guardarProyectos(Proyectos.crear("Proyecto Beta"), idPadre);

        // 2. WHEN (Ejecutamos la consulta del Dashboard)
        PaginationResponseDTO<InmobiliariaDashBoardDto> respuesta =
                dashboardRepository.inmobiliariaListar(1, 10, adminId);

        // 3. THEN (Verificamos)
        assertNotNull(respuesta);
        assertFalse(respuesta.content().isEmpty(), "La lista no debe estar vacía");

        // Buscamos la que acabamos de insertar (por si hay otras en la BD)
        InmobiliariaDashBoardDto encontrado = respuesta.content().stream()
                .filter(dto -> dto.ruc().equals(ruc))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No se encontró la inmobiliaria insertada"));

        // Validaciones Clave
        assertEquals(razonSocial, encontrado.razonSocial());
        assertEquals(2, encontrado.nProyectos(), "El SP debe haber contado 2 proyectos activos");
        assertTrue(encontrado.idInmobiliaria() > 0);
        assertTrue(encontrado.estado());
        assertNotNull(encontrado.fechaModificacion());

        // Validación de Paginación
        assertTrue(respuesta.totalElements() >= 1);
    }

    @Test
    @DisplayName("Debe devolver lista vacía si el Admin no tiene inmobiliarias")
    void listarDashboard_Vacio() {
        // 1. GIVEN
        String adminSinDatos = "ADMIN_FANTASMA_" + System.currentTimeMillis();

        // 2. WHEN
        PaginationResponseDTO<InmobiliariaDashBoardDto> respuesta =
                dashboardRepository.inmobiliariaListar(1, 10, adminSinDatos);

        // 3. THEN
        assertNotNull(respuesta);
        assertTrue(respuesta.content().isEmpty());
        assertEquals(0, respuesta.totalElements());
        assertEquals(0, respuesta.totalPages());
    }
}
