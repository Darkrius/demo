package com.example.demo.api.controller;


import com.example.demo.api.dto.CrearPromotorRequest;
import com.example.demo.api.dto.PromotorResponse;
import com.example.demo.api.mapper.PromotoMapperApi;
import com.example.demo.application.dto.query.PaginacionResponseDto;
import com.example.demo.application.interfaces.asesores.promotor.CrearPromotorCommand;
import com.example.demo.application.interfaces.asesores.promotor.CrearPromotorUseCase;
import com.example.demo.application.interfaces.asesores.promotor.ListarPromotorUseCase;
import com.example.demo.domain.entities.Promotor;
import com.example.demo.application.dto.query.PromotorDashBoard;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotores")
public class PromotorController {

    private final ListarPromotorUseCase listarPromotorUseCase;
    private final PromotoMapperApi promotorMapperApi;
    private final CrearPromotorUseCase crearPromotorUseCase;


    public PromotorController(ListarPromotorUseCase listarPromotorUseCase, PromotoMapperApi promotorMapperApi, CrearPromotorUseCase crearPromotorUseCase) {
        this.listarPromotorUseCase = listarPromotorUseCase;
        this.promotorMapperApi = promotorMapperApi;
        this.crearPromotorUseCase = crearPromotorUseCase;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<PaginacionResponseDto<PromotorResponse>> listarPromotores(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        String idAdminCreador = jwt.getSubject();
        PaginacionResponseDto<PromotorDashBoard> paginaCruda =
                listarPromotorUseCase.listarPorAdmin(idAdminCreador, page, size);
        List<PromotorResponse> contenidoSimple =
                promotorMapperApi.promotorDashBoardToPromotorResponse(paginaCruda.content());
        PaginacionResponseDto<PromotorResponse> paginaMapeada = new PaginacionResponseDto<>(
                contenidoSimple, paginaCruda.currentPage(), paginaCruda.totalPages(), paginaCruda.totalItems()
        );
        return ResponseEntity.ok(paginaMapeada);
    }



    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<PromotorResponse> crearPromotor(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid CrearPromotorRequest crearPromotorRequest
    ) {
        String idAdminCreador = jwt.getSubject();
        CrearPromotorCommand command = promotorMapperApi.crearPromotorRequestToCrearPromotorCommand(crearPromotorRequest);
        Promotor promotorCreado = crearPromotorUseCase.crear(command, idAdminCreador);
        PromotorResponse response = promotorMapperApi.promotorToPromotorResponse(promotorCreado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
