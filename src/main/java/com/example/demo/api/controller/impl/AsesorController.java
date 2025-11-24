package com.example.demo.api.controller.impl;

import com.example.demo.api.controller.interfaces.AsesorApi;
import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.commands.RegistrarAsesorCommand;
import com.example.demo.application.dto.queries.AsesorDashBoardDto;
import com.example.demo.application.interfaces.usecases.ListarAsesorService;
import com.example.demo.application.interfaces.usecases.ListarAsesoresExternosService;
import com.example.demo.application.interfaces.usecases.RegistrarAsesorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/asesores")
@Slf4j
public class AsesorController implements AsesorApi {


    private final ListarAsesorService listarAsesorService;
    private final ListarAsesoresExternosService asesoresExternosService;
    private final RegistrarAsesorService registrarAsesorService;

    public AsesorController(ListarAsesorService listarAsesorService, ListarAsesoresExternosService asesoresExternosService, RegistrarAsesorService registrarAsesorService) {
        this.listarAsesorService = listarAsesorService;
        this.asesoresExternosService = asesoresExternosService;
        this.registrarAsesorService = registrarAsesorService;
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<PaginationResponseDTO<?>> listar(int page, int size, String nombre, String ciudad, String origen) {
        log.info("API: Solicitud de listado recibida. Origen: [{}], Page: [{}], Size: [{}]", origen, page, size);

        if ("EXTERNO".equalsIgnoreCase(origen)) {
            log.debug("API: Redirigiendo a flujo EXTERNO (Candidatos)");
            var response = asesoresExternosService.listarCandidatosExternos(page, size, nombre, ciudad);
            return ResponseEntity.ok(response);

        } else {
            log.debug("API: Redirigiendo a flujo INTERNO (Dashboard)");
            var response = listarAsesorService.listarAsesoresGestion(page, size, nombre);
            return ResponseEntity.ok(response);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<AsesorDashBoardDto> registrar(RegistrarAsesorCommand command) {
        log.info("API: Solicitud de registro recibida para ID: [{}]", command.idAsesorAD());

        AsesorDashBoardDto creado = registrarAsesorService.registrarAsesor(command);

        log.info("API: Registro completado. Retornando 201 Created.");
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    }

