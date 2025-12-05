package com.example.demo.api.controller;


import com.example.demo.api.dto.request.EditarInmobiliariaRequest;
import com.example.demo.api.dto.request.InmobiliariaRequest;
import com.example.demo.api.mapper.InmobiliriaApiMapper;
import com.example.demo.application.dto.commands.RegistrarInmobiliariaCommand;
import com.example.demo.application.dto.queries.InmobiliariaDashBoardDto;
import com.example.demo.application.dto.queries.InmobiliariaDetalleDto;
import com.example.demo.application.exceptions.EntidadDuplicadaException;
import com.example.demo.application.exceptions.RecursoNoEncontradoException;
import com.example.demo.application.interfaces.usecases.CrearInmobiliariaService;
import com.example.demo.application.interfaces.usecases.EditarInmobiliariaService;
import com.example.demo.application.services.DetalleInmobiliariaServiceImpl;
import com.example.demo.domain.dto.EditarInmobiliaria;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // Spring Boot 3.4+
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt; // <--- VITAL PARA JWT
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // Usamos contexto completo para evitar problemas de Proxy
@AutoConfigureMockMvc
class InmobiliariaControllerTest {



    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Mockeamos las dependencias del controlador
    @MockitoBean
    private CrearInmobiliariaService crearService;

    @MockitoBean
    private InmobiliriaApiMapper mapper;

    @MockitoBean
    private DetalleInmobiliariaServiceImpl detalleInmobiliariaService;

    @MockitoBean
    private EditarInmobiliariaService editarInmobiliariaService;

    // -------------------------------------------------------------------
    // TEST 1: REGISTRO EXITOSO (ADMIN)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("POST /inmobiliarias -> 201 Created (Con Logo y JSON)")
    void registrar_Exito() throws Exception {

        // 1. GIVEN: Preparamos los datos de entrada (Multipart)
        // A. El Archivo (Logo)
        MockMultipartFile file = new MockMultipartFile(
                "logo", "logo.png", "image/png", "contenido_imagen_fake".getBytes()
        );

        // B. El JSON (Data) -> OJO: Se envía como otro archivo con content-type json
        InmobiliariaRequest requestDto = new InmobiliariaRequest(
                "20123456789", "Inmo Test", List.of("Proyecto 1")
        );
        MockMultipartFile metadata = new MockMultipartFile(
                "data", "", "application/json", objectMapper.writeValueAsBytes(requestDto)
        );

        // C. Simulamos la respuesta del Mapper y Servicio
        RegistrarInmobiliariaCommand command = new RegistrarInmobiliariaCommand("20123456789", "Inmo Test", List.of("P1"), null);
        InmobiliariaDashBoardDto responseDto = new InmobiliariaDashBoardDto(1L, "20123456789", "Inmo Test", 1, true, LocalDateTime.now());

        when(mapper.toCommand(any(), any())).thenReturn(command);
        // Importante: Validamos que pase el ID del token
        when(crearService.crearInmobiliaria(any(), eq("ADMIN_ID_FROM_TOKEN"))).thenReturn(responseDto);

        // 2. WHEN & THEN
        mockMvc.perform(multipart(HttpMethod.POST, "/api/v1/inmobiliarias")
                        .file(file)
                        .file(metadata)
                        .with(csrf())
                        // SIMULAMOS UN JWT TOKEN VÁLIDO DE ADMIN
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN"))
                                .jwt(token -> token.subject("ADMIN_ID_FROM_TOKEN")))) // <--- Aquí inyectamos el ID
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idInmobiliaria").value(1));
    }

    // -------------------------------------------------------------------
    // TEST 2: ERROR DE VALIDACIÓN (400)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("POST /inmobiliarias -> 400 Bad Request (RUC Inválido)")
    void registrar_RucInvalido() throws Exception {

        // JSON con RUC malo
        InmobiliariaRequest requestDto = new InmobiliariaRequest(
                "123", "Inmo Test", null
        );
        MockMultipartFile metadata = new MockMultipartFile(
                "data", "", "application/json", objectMapper.writeValueAsBytes(requestDto)
        );

        mockMvc.perform(multipart(HttpMethod.POST, "/api/v1/inmobiliarias")
                        .file(metadata) // Enviamos sin logo (es opcional)
                        .with(csrf())
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN"))))
                .andExpect(status().isBadRequest()); // @Valid debe saltar antes de llegar al controller
    }

    // -------------------------------------------------------------------
    // TEST 3: SEGURIDAD (403 FORBIDDEN)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("POST /inmobiliarias -> 403 Forbidden (Usuario normal)")
    void registrar_SinPermisos() throws Exception {

        InmobiliariaRequest requestDto = new InmobiliariaRequest(
                "20123456789",
                "Inmobiliaria Fake SAC",
                null
        );

        // Convertimos el objeto a JSON bytes para el Multipart
        MockMultipartFile metadata = new MockMultipartFile(
                "data",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(requestDto)
        );

        // 2. WHEN & THEN
        mockMvc.perform(multipart(HttpMethod.POST, "/api/v1/inmobiliarias")
                        .file(metadata)
                        .with(csrf())
                        // SIMULAMOS USUARIO SIN PERMISOS (SCOPE_USER)
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_USER"))))
                .andExpect(status().isForbidden()); // <--- ¡AHORA SÍ DARÁ 403!
    }

    // -------------------------------------------------------------------
    // TEST 4: DUPLICADO (409 CONFLICT)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("POST /inmobiliarias -> 409 Conflict (RUC ya existe)")
    void registrar_Duplicado() throws Exception {

        // Preparamos datos válidos
        InmobiliariaRequest requestDto = new InmobiliariaRequest("20123456789", "Test", null);
        MockMultipartFile metadata = new MockMultipartFile("data", "", "application/json", objectMapper.writeValueAsBytes(requestDto));

        // Simulamos que el servicio lanza la excepción
        when(mapper.toCommand(any(), any())).thenReturn(new RegistrarInmobiliariaCommand("20123456789", "Test", null, null));
        when(crearService.crearInmobiliaria(any(), any()))
                .thenThrow(new EntidadDuplicadaException("RUC Duplicado"));

        mockMvc.perform(multipart(HttpMethod.POST, "/api/v1/inmobiliarias")
                        .file(metadata)
                        .with(csrf())
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN"))
                                .jwt(t -> t.subject("ADMIN"))))
                .andExpect(status().isConflict()); // El Advice debe capturarlo
    }

    @Test
    @DisplayName("GET /{id} -> Debe retornar detalle completo de Inmobiliaria con sus listas")
    void obtenerPorId_Exito() throws Exception {
        // 1. GIVEN
        Long idInmo = 18L;

        var proyectoDto = new InmobiliariaDetalleDto.ProyectosDto(
                100L, "Residencial Sol"
        );

        var promotorDto = new InmobiliariaDetalleDto.PromotoresProyectoDto(
                100L, "Residencial Sol", 50L, "Juan Perez"
        );

        // Preparamos el DTO Padre
        // --- CORRECCIÓN AQUÍ ---
        InmobiliariaDetalleDto detalleMock = new InmobiliariaDetalleDto(
                idInmo,
                "20601234561",
                "Inmobiliaria Los Andes",
                true, // <--- AGREGAR ESTE BOOLEAN (Estado)
                List.of(proyectoDto),      // Lista de Proyectos
                List.of(promotorDto)       // Lista de Promotores
        );
        // -----------------------

        // Configuramos el comportamiento del Mock
        when(detalleInmobiliariaService.listar(idInmo)).thenReturn(detalleMock);

        // 2. WHEN & THEN
        mockMvc.perform(get("/api/v1/inmobiliarias/{id}", idInmo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN"))))
                .andExpect(status().isOk())
                // Validamos Cabecera
                .andExpect(jsonPath("$.idInmobiliaria").value(18))
                .andExpect(jsonPath("$.razonSocial").value("Inmobiliaria Los Andes"))
                .andExpect(jsonPath("$.estado").value(true)) // Opcional: Validar que llegue el estado
                // Validamos Lista de Proyectos
                .andExpect(jsonPath("$.proyectos[0].nombreProyecto").value("Residencial Sol"))
                // Validamos Lista de Promotores
                .andExpect(jsonPath("$.promotoresProyectos[0].nombrePromotor").value("Juan Perez"));

        // Verificamos que se llamó al servicio correcto
        verify(detalleInmobiliariaService).listar(idInmo);
    }

    // -------------------------------------------------------------------
    // TEST: NO ENCONTRADO (404)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("GET /{id} -> Debe retornar 404 si no existe")
    void obtenerPorId_NoEncontrado() throws Exception {
        Long idNoExiste = 999L;

        when(detalleInmobiliariaService.listar(idNoExiste))
                .thenThrow(new RecursoNoEncontradoException("Inmobiliaria no encontrada"));

        mockMvc.perform(get("/api/v1/inmobiliarias/{id}", idNoExiste)
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN"))))
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("PATCH /{id} -> 200 OK: Edita y devuelve objeto actualizado")
    void editar_Exito() throws Exception {
        // 1. GIVEN
        Long idInmo = 10L;

        // A. Request JSON (Lo que envía el usuario)
        EditarInmobiliariaRequest request = new EditarInmobiliariaRequest(
                true,
                List.of("Torre Nueva"),
                List.of(5L)
        );

        // B. Command (Lo que devuelve el Mapper)
        EditarInmobiliaria command = new EditarInmobiliaria(idInmo, true, List.of("Torre Nueva"), List.of(5L));

        // C. Response DTO (Lo que devuelve el Servicio tras editar y recargar)
        // Usamos un objeto dummy para validar que el controller lo pasa tal cual
        InmobiliariaDetalleDto respuestaEsperada = new InmobiliariaDetalleDto(
                idInmo, "20100000000", "Inmo Editada", true,
                List.of(new InmobiliariaDetalleDto.ProyectosDto(500L, "Torre Nueva")), // Proyecto nuevo generado
                List.of()
        );

        // Configuramos Mocks
        when(mapper.toCommandEditar(any(EditarInmobiliariaRequest.class), eq(idInmo))).thenReturn(command);
        when(editarInmobiliariaService.editar(any(EditarInmobiliaria.class))).thenReturn(respuestaEsperada);

        // 2. WHEN & THEN
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/v1/inmobiliarias/{id}", idInmo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)) // Convertimos DTO a JSON string
                        .with(csrf()) // Seguridad CSRF
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN")))) // Rol ADMIN requerido
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idInmobiliaria").value(idInmo))
                .andExpect(jsonPath("$.razonSocial").value("Inmo Editada"))
                .andExpect(jsonPath("$.proyectos[0].idProyecto").value(500))
                .andExpect(jsonPath("$.proyectos[0].nombreProyecto").value("Torre Nueva"));

        // Verificaciones
        verify(mapper).toCommandEditar(any(), eq(idInmo));
        verify(editarInmobiliariaService).editar(any());
    }

    // -------------------------------------------------------------------
    // TEST 6: EDICIÓN PROHIBIDA (ROL USUARIO)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("PATCH /{id} -> 403 Forbidden: Usuario normal no puede editar")
    void editar_SinPermisos() throws Exception {
        Long idInmo = 10L;
        EditarInmobiliariaRequest request = new EditarInmobiliariaRequest(true, null, null);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/v1/inmobiliarias/{id}", idInmo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        // SIMULAMOS USUARIO SIN PERMISOS (SCOPE_USER)
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_USER"))))
                .andExpect(status().isForbidden());
    }

    // -------------------------------------------------------------------
    // TEST 7: EDICIÓN FALLIDA (REGLA DE NEGOCIO)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("PATCH /{id} -> 409 Conflict: Cuando se intenta duplicar un proyecto")
    void editar_Duplicado() throws Exception {
        // GIVEN
        Long idInmo = 10L;
        EditarInmobiliariaRequest request = new EditarInmobiliariaRequest(true, List.of("Torre A"), null);

        // Mocks
        when(mapper.toCommandEditar(any(), eq(idInmo)))
                .thenReturn(new EditarInmobiliaria(idInmo, true, List.of("Torre A"), null));

        when(editarInmobiliariaService.editar(any()))
                .thenThrow(new EntidadDuplicadaException("El proyecto 'Torre A' ya existe en esta inmobiliaria"));

        // WHEN & THEN
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/v1/inmobiliarias/{id}", idInmo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN"))))
                .andExpect(status().isConflict()) // HTTP 409
                // CORRECCIÓN: Validamos tus campos personalizados
                .andExpect(jsonPath("$.codigo").value("409"))
                .andExpect(jsonPath("$.mensaje").value("El proyecto 'Torre A' ya existe en esta inmobiliaria"));
    }

    // -------------------------------------------------------------------
    // TEST 9: NO ENCONTRADO (404) - ID INEXISTENTE
    // -------------------------------------------------------------------
    @Test
    @DisplayName("PATCH /{id} -> 404 Not Found: Cuando el ID no existe")
    void editar_NoEncontrado() throws Exception {
        // GIVEN
        Long idFantasma = 9999L;
        EditarInmobiliariaRequest request = new EditarInmobiliariaRequest(true, null, null);

        when(mapper.toCommandEditar(any(), eq(idFantasma)))
                .thenReturn(new EditarInmobiliaria(idFantasma, true, null, null));

        when(editarInmobiliariaService.editar(any()))
                .thenThrow(new RecursoNoEncontradoException("No se encontró la inmobiliaria con ID " + idFantasma));

        // WHEN & THEN
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/v1/inmobiliarias/{id}", idFantasma)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN"))))
                .andExpect(status().isNotFound()) // HTTP 404
                // CORRECCIÓN: Validamos tus campos personalizados
                .andExpect(jsonPath("$.codigo").value("404"))
                .andExpect(jsonPath("$.mensaje").value("No se encontró la inmobiliaria con ID 9999"));
    }

    // -------------------------------------------------------------------
    // TEST 10: ERROR INTERNO (500) - FALLO BASE DE DATOS
    // -------------------------------------------------------------------
    @Test
    @DisplayName("PATCH /{id} -> 500 Internal Server Error: Fallo inesperado")
    void editar_ErrorInterno() throws Exception {
        // GIVEN
        Long idInmo = 10L;
        EditarInmobiliariaRequest request = new EditarInmobiliariaRequest(true, null, null);

        when(mapper.toCommandEditar(any(), eq(idInmo)))
                .thenReturn(new EditarInmobiliaria(idInmo, true, null, null));

        when(editarInmobiliariaService.editar(any()))
                .thenThrow(new RuntimeException("Error de conexión con base de datos"));

        // WHEN & THEN
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/v1/inmobiliarias/{id}", idInmo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN"))))
                .andExpect(status().isInternalServerError()) // HTTP 500
                // CORRECCIÓN: Validamos tus campos personalizados
                .andExpect(jsonPath("$.codigo").value("500"))
                // Nota: Tu GlobalHandler probablemente oculta el mensaje real "Error conexión..." y pone uno genérico
                .andExpect(jsonPath("$.mensaje").value("Ocurrió un error interno al procesar la solicitud."));
    }

    // -------------------------------------------------------------------
    // TEST 7: EDICIÓN FALLIDA (REGLA DE NEGOCIO - 400)
    // -------------------------------------------------------------------
    @Test
    @DisplayName("PATCH /{id} -> 400 Bad Request: Error de regla de negocio")
    void editar_ErrorReglaNegocio() throws Exception {
        Long idInmo = 10L;
        EditarInmobiliariaRequest request = new EditarInmobiliariaRequest(false, null, null);

        when(mapper.toCommandEditar(any(), any())).thenReturn(new EditarInmobiliaria(idInmo, false, null, null));

        when(editarInmobiliariaService.editar(any()))
                .thenThrow(new com.example.demo.application.exceptions.ReglasNegocioException("No se puede desactivar con promotores activos"));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/v1/inmobiliarias/{id}", idInmo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN"))))
                .andExpect(status().isBadRequest()) // HTTP 400
                // CORRECCIÓN: Validamos tus campos personalizados
                .andExpect(jsonPath("$.codigo").value("400"))
                .andExpect(jsonPath("$.mensaje").value("No se puede desactivar con promotores activos"));
    }
}
