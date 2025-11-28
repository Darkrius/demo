package com.example.demo.api.controller;

import com.example.demo.api.mapper.PromotorApiMapper;
import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.commands.RegistrarPromotorCommand;
import com.example.demo.application.dto.queries.PromotorDashBoardDto;
import com.example.demo.application.exceptions.EntidadDuplicadaException;
import com.example.demo.application.interfaces.usecases.ListarPromotorService;
import com.example.demo.application.interfaces.usecases.RegistrarPromotorService;
import com.example.demo.infraestructure.config.LegacyDataSourceConfig;
import com.example.demo.infraestructure.config.PrincipalDataSourceConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import static org.mockito.ArgumentMatchers.eq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc // Configura MockMvc
@Tag("integration")
@Import({PrincipalDataSourceConfig.class, LegacyDataSourceConfig.class})
class PromotorControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    // Mocks de la Capa Application (Aislamos el Controller)
    @MockitoBean private RegistrarPromotorService registrarService;
    @MockitoBean private ListarPromotorService listarService;
    @MockitoBean private PromotorApiMapper mapper; // Necesario para que el contexto cargue

    private final String API_URL = "/api/v1/promotores";
    private final String ADMIN_ID = "ADM-123";

    // Helper: Datos válidos que pasan la validación @Valid
    private String crearJsonRequest(String doi, String correo) throws Exception {
        // Usamos el DTO de API Request que creamos para la validación
        var requestDto = new com.example.demo.api.dto.request.PromotorRequest(
                "Juan", "Perez", doi, correo, 1L, Collections.emptyList()
        );
        return objectMapper.writeValueAsString(requestDto);
    }

    // -------------------------------------------------------------------
    // TEST 1: REGISTRAR (Happy Path)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("POST /registrar -> Debe retornar 201 Created y mapear el ID de usuario")
    void registrar_Exito() throws Exception {
        // GIVEN
        String requestJson = crearJsonRequest("87654321", "nuevo@test.com");

        // Simulamos el DTO de retorno (con el ID generado)
        PromotorDashBoardDto responseDto = new PromotorDashBoardDto(
                50L, "Juan Perez", "Cargando...", true, LocalDateTime.now()
        );

        // Mockeamos la conversión a Command y la llamada al servicio
        when(mapper.toCommand(any())).thenReturn(new RegistrarPromotorCommand("J", "P", "8", "c@c.c", 1L, null));
        when(registrarService.registrar(any(), anyString())).thenReturn(responseDto);

        // WHEN & THEN
        mockMvc.perform(post(API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN"))
                                .jwt(t -> t.subject(ADMIN_ID))))
                .andExpect(status().isCreated()) // Esperamos 201
                .andExpect(jsonPath("$.idPromotor").value(50)) // O idUsuario, dependiendo del DTO
                .andExpect(jsonPath("$.nombreCompleto").value("Juan Perez"));

        // Verificamos que se llamó al servicio con el ID del Admin
        verify(registrarService).registrar(any(), eq(ADMIN_ID));
    }

    // -------------------------------------------------------------------
    // TEST 2: REGISTRAR (Security Failure)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("POST /registrar -> 403 Forbidden si rol es insuficiente")
    void registrar_SinPermisos() throws Exception {
        String requestJson = crearJsonRequest("87654321", "a@a.com");

        mockMvc.perform(post(API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        // ROL INCORRECTO: Debería ser SCOPE_ADMIN
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_USER"))))
                .andExpect(status().isForbidden()); // Esperamos 403
    }

    // -------------------------------------------------------------------
    // TEST 3: REGISTRAR (Business Conflict - 409)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("POST /registrar -> 409 Conflict si el DOI ya existe")
    void registrar_ConflictoDuplicado() throws Exception {
        String requestJson = crearJsonRequest("87654321", "a@a.com");

        // Simulamos que el servicio lanza el error de duplicado
        when(registrarService.registrar(any(), anyString()))
                .thenThrow(new EntidadDuplicadaException("DOI/Correo ya existe."));

        mockMvc.perform(post(API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN"))))
                .andExpect(status().isConflict()) // Esperamos 409
                .andExpect(jsonPath("$.codigo").value("409"));
    }

    // -------------------------------------------------------------------
    // TEST 4: LISTAR DASHBOARD (GET Success)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("GET /listar -> 200 OK y filtra por Admin")
    void listarPromotores_Exito() throws Exception {

        String idAdmin = "ADM-123";
        // Preparamos la respuesta (Dashboard DTO)
        PaginationResponseDTO<PromotorDashBoardDto> mockResponse = new PaginationResponseDTO<>(
                List.of(new PromotorDashBoardDto(1L, "Promotor Name", "Empresa A", true, LocalDateTime.now())),
                1, 10, 1, 1, true
        );
        when(listarService.listarPromotor(idAdmin, 1, 10)).thenReturn(mockResponse);

        // Ejecutamos la petición GET
        mockMvc.perform(get(API_URL)
                        .param("page", "1")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN"))
                                .jwt(t -> t.subject(idAdmin))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombreInmobiliaria").value("Empresa A"))
                .andExpect(jsonPath("$.totalElements").value(1));

        // Verificamos que se llamó al servicio con el ID del Admin para el filtro
        verify(listarService).listarPromotor(idAdmin, 1, 10);
    }
}
