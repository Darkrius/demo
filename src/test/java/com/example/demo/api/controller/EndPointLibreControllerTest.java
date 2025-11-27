package com.example.demo.api.controller;


import com.example.demo.application.dto.SelectorDto;
import com.example.demo.application.interfaces.usecases.SelectorService;
import com.example.demo.infraestructure.config.LegacyDataSourceConfig;
import com.example.demo.infraestructure.config.PrincipalDataSourceConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")
@Import({PrincipalDataSourceConfig.class, LegacyDataSourceConfig.class})
class EndPointLibreControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Mockeamos el Servicio porque NO queremos probar la base de datos aquí.
    // Solo probamos que el Controlador reciba la petición, valide seguridad y llame al servicio.
    @MockitoBean
    private SelectorService selectorUseCase;

    // -------------------------------------------------------------------
    // TEST 1: SELECTOR INMOBILIARIAS (Requiere extracción de ID del token)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("GET /inmobiliarias -> 200 OK y pasa el ID del token al servicio")
    void listarInmobiliarias_Exito() throws Exception {
        // 1. GIVEN
        String idAdminEnToken = "ADMIN_001";
        List<SelectorDto> mockLista = List.of(
                new SelectorDto(1L, "Inmobiliaria Los Andes"),
                new SelectorDto(2L, "Grupo Centenario")
        );

        // Configuramos el mock del servicio
        when(selectorUseCase.obtenerInmobiliaria(idAdminEnToken)).thenReturn(mockLista);

        // 2. WHEN & THEN
        mockMvc.perform(get("/api/v1/giovanni")
                        .contentType(MediaType.APPLICATION_JSON)
                        // Simulamos un JWT válido:
                        // - authorities: SCOPE_ADMIN (para pasar el @PreAuthorize)
                        // - jwt subject: ADMIN_001 (para que el controller lo extraiga)
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN"))
                                .jwt(token -> token.subject(idAdminEnToken))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].descripcion").value("Inmobiliaria Los Andes"));

        // Verificación clave: ¿El controller extrajo "ADMIN_001" del token y se lo dio al servicio?
        verify(selectorUseCase).obtenerInmobiliaria(idAdminEnToken);
    }

    // -------------------------------------------------------------------
    // TEST 2: SELECTOR PROYECTOS (Usa PathVariable)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("GET /proyectos-selector -> 200 OK con ID de inmobiliaria")
    void listarProyectos_Exito() throws Exception {
        // 1. GIVEN
        Long idInmo = 50L;
        List<SelectorDto> mockLista = List.of(
                new SelectorDto(100L, "Proyecto Sol"),
                new SelectorDto(101L, "Proyecto Luna")
        );

        when(selectorUseCase.obtenerProyecto(idInmo)).thenReturn(mockLista);

        // 2. WHEN & THEN
        mockMvc.perform(get("/api/v1/giovanni/{id}/proyectos-selector", idInmo)
                        .contentType(MediaType.APPLICATION_JSON)
                        // Aquí solo necesitamos el rol, el ID del usuario no se usa en este método
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].descripcion").value("Proyecto Sol"));

        verify(selectorUseCase).obtenerProyecto(idInmo);
    }

    // -------------------------------------------------------------------
    // TEST 3: SEGURIDAD (Forbidden)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("GET /inmobiliarias -> 403 Forbidden si el usuario no es ADMIN")
    void listarInmobiliarias_SinPermiso() throws Exception {
        // Intentamos entrar con un usuario normal (SCOPE_USER)
        mockMvc.perform(get("/api/v1/giovanni")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_USER"))))
                .andExpect(status().isForbidden()); // Debe rebotar
    }

}
