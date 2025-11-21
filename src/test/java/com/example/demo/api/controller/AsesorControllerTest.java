package com.example.demo.api.controller;


import com.example.demo.api.dto.AsesorSimpleResponseDto;
import com.example.demo.api.mapper.AsesorApiMapper;
import com.example.demo.application.dto.query.PaginacionResponseDto;
import com.example.demo.application.interfaces.asesores.ListarAsesorUseCase;
import com.example.demo.domain.entities.AsesorExterno;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(authorities = "SCOPE_ADMIN")
@WebMvcTest(controllers = AsesorController.class)
class AsesorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ListarAsesorUseCase useCase;

    @Autowired
    private AsesorApiMapper apiMapper;

    @TestConfiguration
    static class MockConfig {

        @Bean
        public ListarAsesorUseCase useCaseMock() {
            return Mockito.mock(ListarAsesorUseCase.class);
        }

        @Bean
        public AsesorApiMapper mapperMock() {
            return Mockito.mock(AsesorApiMapper.class);
        }
    }

    @Test
    void obtenerAsesoresExternos_OK() throws Exception {

        // Datos dominio
        AsesorExterno asesor = new AsesorExterno();
        asesor.setIdAsesorAD("AD001");
        asesor.setNombres("Juan");
        asesor.setApellidos("Perez");
        asesor.setCiudad("Lima");

        PaginacionResponseDto<AsesorExterno> paginaDominio =
                new PaginacionResponseDto<>(
                        List.of(asesor), 1, 1, 1);

        // Datos de API
        AsesorSimpleResponseDto dto =
                new AsesorSimpleResponseDto("AD001", "Juan", "Perez", "Lima");

        // Mock caso de uso
        when(useCase.listarAsesoresPage("Juan", "Lima", 1, 10))
                .thenReturn(paginaDominio);

        // Mock mapper
        when(apiMapper.toSimpleDtoList(List.of(asesor)))
                .thenReturn(List.of(dto));

        when(apiMapper.toSimplePaginacionDto(paginaDominio))
                .thenReturn(new PaginacionResponseDto<>(
                        List.of(dto), 1, 1, 1
                ));

        mockMvc.perform(get("/api/gestion/asesores-externos")
                        .param("nombre", "Juan")
                        .param("ciudad", "Lima")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].idAsesorAD").value("AD001"))
                .andExpect(jsonPath("$.content[0].nombres").value("Juan"))
                .andExpect(jsonPath("$.content[0].apellidos").value("Perez"))
                .andExpect(jsonPath("$.content[0].ciudad").value("Lima"))
                .andExpect(jsonPath("$.totalItems").value(1));
    }
}
