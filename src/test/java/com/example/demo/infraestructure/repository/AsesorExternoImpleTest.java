package com.example.demo.infraestructure.repository;


import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.AsesorLegacyDto;
import com.example.demo.infraestructure.config.LegacyDataSourceConfig;
import com.example.demo.infraestructure.config.PrincipalDataSourceConfig;
import com.example.demo.infraestructure.persistence.repositorys.AsesorExternoImple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

@Tag("integration")
@Import({PrincipalDataSourceConfig.class, LegacyDataSourceConfig.class})
class AsesorExternoImpleTest {

    @Autowired
    private AsesorExternoImple repository;

    @Test
    @DisplayName("Debe listar candidatos externos y traer el total correcto")
    void listarCandidatos_Exitoso() {
        // 1. GIVEN (Datos de entrada)
        int page = 1;
        int size = 10;
        String nombre = null; // Traer todos (o pon un nombre que sepas que existe en tu BD Legacy)
        String ciudad = null;

        // 2. WHEN (Ejecución real contra SQL Server)
        PaginationResponseDTO<AsesorLegacyDto> response = repository.listar(page, size, nombre, ciudad);

        // 3. THEN (Verificación)
        assertNotNull(response, "La respuesta no debe ser nula");
        assertNotNull(response.content(), "La lista de contenido no debe ser nula");

        // Si tu base de datos Legacy tiene datos, esto debería ser true
        if (!response.content().isEmpty()) {
            AsesorLegacyDto primerCandidato = response.content().get(0);

            System.out.println("Candidato encontrado: " + primerCandidato.nombreCompleto());

            assertNotNull(primerCandidato.idAsesorAD(), "El ID del candidato no puede ser nulo");
            assertNotNull(primerCandidato.ciudad(), "La ciudad no puede ser nula");

            // Verificar que el totalElements tenga sentido (mayor o igual al tamaño de la página actual)
            assertTrue(response.totalElements() >= response.content().size(),
                    "El total de elementos debe ser coherente");
        } else {
            System.out.println("ADVERTENCIA: La tabla Asesores_Externos está vacía. El test pasó pero no validó datos.");
        }
    }

    @Test
    @DisplayName("Debe devolver lista vacía si el filtro no coincide con nada")
    void listarCandidatos_SinResultados() {
        // 1. GIVEN
        String nombreImposible = "XyzNombreQueNoExiste123";

        // 2. WHEN
        PaginationResponseDTO<AsesorLegacyDto> response = repository.listar(1, 10, nombreImposible, null);

        // 3. THEN
        assertNotNull(response);
        assertTrue(response.content().isEmpty(), "La lista debería estar vacía");
        assertEquals(0, response.totalElements(), "El total debería ser 0");
    }
}
