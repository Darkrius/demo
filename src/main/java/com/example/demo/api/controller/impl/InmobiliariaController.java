package com.example.demo.api.controller.impl;

import com.example.demo.api.controller.interfaces.InmobiliariaApi;
import com.example.demo.api.dto.request.InmobiliariaRequest;
import com.example.demo.api.mapper.InmobiliriaApiMapper;
import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.commands.RegistrarInmobiliariaCommand;
import com.example.demo.application.dto.DatosEmpresaDto;
import com.example.demo.application.dto.queries.InmobiliariaDashBoardDto;
import com.example.demo.application.interfaces.usecases.ConsultarRucService;
import com.example.demo.application.interfaces.usecases.CrearInmobiliariaService;
import com.example.demo.application.interfaces.usecases.ListarInmobiliaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class InmobiliariaController implements InmobiliariaApi {

    private final CrearInmobiliariaService crearInmobiliariaService;
    private final InmobiliriaApiMapper inmobiliriaApiMapper;
    private final ConsultarRucService consultarRucUseCase;
    private final ListarInmobiliaService listarService;

    public InmobiliariaController(CrearInmobiliariaService crearInmobiliariaService, InmobiliriaApiMapper inmobiliriaApiMapper, ConsultarRucService consultarRucUseCase, ListarInmobiliaService listarService) {
        this.crearInmobiliariaService = crearInmobiliariaService;
        this.inmobiliriaApiMapper = inmobiliriaApiMapper;
        this.consultarRucUseCase = consultarRucUseCase;
        this.listarService = listarService;
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<InmobiliariaDashBoardDto> registrar(InmobiliariaRequest request, MultipartFile logo,
                                                              @AuthenticationPrincipal Jwt jwt) {
        String idAdmin = jwt.getSubject();
        log.info("API: Solicitud de crear Inmobiliaria recibida. RUC: [{}]", request.ruc());

        // 2. MAPPER: Convierte (JSON + File) -> Command Puro
        RegistrarInmobiliariaCommand command = inmobiliriaApiMapper.toCommand(request, logo);

        // 3. SERVICE: Ejecuta la lógica de negocio
        InmobiliariaDashBoardDto creado = crearInmobiliariaService.crearInmobiliaria(command, idAdmin);

        log.info("API: Inmobiliaria creada. ID: [{}]", creado.idInmobiliaria()); // Usando tu nombre de campo

        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<DatosEmpresaDto> consultarRuc(String ruc) {
        log.info("API: Solicitud de consulta RUC recibida: [{}]", ruc);

        DatosEmpresaDto respuesta = consultarRucUseCase.obtenerRazonSocial(ruc);

        return ResponseEntity.ok(respuesta);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<PaginationResponseDTO<InmobiliariaDashBoardDto>> listar(int page, int size, Jwt jwt) {
        String idAdmin = jwt.getSubject();
        log.info("API: Listando inmobiliarias. Admin: [{}], Page: [{}]", idAdmin, page);

        // 2. Llamar al Servicio (Application)
        // El servicio delega a Infra, Infra ejecuta el SP, Infra devuelve el DTO paginado.
        PaginationResponseDTO<InmobiliariaDashBoardDto> response =
                listarService.listarInmobiliaria(idAdmin, page, size);

        // 3. Responder
        return ResponseEntity.ok(response);
    }



}
