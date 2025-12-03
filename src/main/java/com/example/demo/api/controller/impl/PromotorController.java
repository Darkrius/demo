package com.example.demo.api.controller.impl;

import com.example.demo.api.controller.interfaces.PromotorApi;
import com.example.demo.api.dto.request.PromotorRequest;
import com.example.demo.api.mapper.PromotorApiMapper;
import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.commands.RegistrarPromotorCommand;
import com.example.demo.application.dto.queries.PromotorDashBoardDto;
import com.example.demo.application.dto.queries.PromotorDetalleDto;
import com.example.demo.application.interfaces.usecases.DetallePromotorService;
import com.example.demo.application.interfaces.usecases.ListarPromotorService;
import com.example.demo.application.interfaces.usecases.RegistrarPromotorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class PromotorController implements PromotorApi {

    private final RegistrarPromotorService registrarPromotorService;

    private final ListarPromotorService listarPromotorService;

    private final PromotorApiMapper promotorApiMapper;

    private final DetallePromotorService obtenerDetalleService;


    public PromotorController(RegistrarPromotorService registrarPromotorService, ListarPromotorService listarPromotorService, PromotorApiMapper promotorApiMapper, DetallePromotorService obtenerDetalleService) {
        this.registrarPromotorService = registrarPromotorService;
        this.listarPromotorService = listarPromotorService;
        this.promotorApiMapper = promotorApiMapper;
        this.obtenerDetalleService = obtenerDetalleService;
    }


    @Override
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<PromotorDashBoardDto> registrar(PromotorRequest request, Jwt jwt) {
        String idAdmin = jwt.getSubject();
        log.info("API: Solicitud de registro Promotor. Admin: [{}], DOI: [{}]", idAdmin, request.doi());
        RegistrarPromotorCommand command = promotorApiMapper.toCommand(request);
        PromotorDashBoardDto creado = registrarPromotorService.registrar(command, idAdmin);
        log.info("API: Registro Promotor exitoso. ID: [{}]", creado.idPromotor());

        return ResponseEntity.status(HttpStatus.CREATED).body(creado);

    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<PaginationResponseDTO<PromotorDashBoardDto>> listar(int page, int size, Jwt jwt) {
        String idAdmin = jwt.getSubject();
        log.info("API: Listar dashboard solicitado por: {} (Page: {})", idAdmin, page);

        PaginationResponseDTO<PromotorDashBoardDto> response =
                listarPromotorService.listarPromotor(idAdmin, page, size);

        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<PromotorDetalleDto> obtenerPorId(Long idUsuario) {
        log.info("API: Solicitud de detalle para Promotor ID: [{}]", idUsuario);

        PromotorDetalleDto detalle = obtenerDetalleService.listar(idUsuario);

        return ResponseEntity.ok(detalle);
    }
}
