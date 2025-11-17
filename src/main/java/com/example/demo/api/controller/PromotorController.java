package com.example.demo.api.controller;


import com.example.demo.api.dto.AsesorSimpleResponseDto;
import com.example.demo.api.dto.PromotorResponse;
import com.example.demo.api.mapper.PromotoMapperApi;
import com.example.demo.application.dto.PaginacionResponseDto;
import com.example.demo.application.interfaces.asesores.promotor.ListarPromotorUseCase;
import com.example.demo.domain.entities.Promotor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/promotores")
public class PromotorController {

    private final ListarPromotorUseCase listarPromotorUseCase;
    private final PromotoMapperApi promotorMapperApi;


    public PromotorController(ListarPromotorUseCase listarPromotorUseCase, PromotoMapperApi promotorMapperApi) {
        this.listarPromotorUseCase = listarPromotorUseCase;
        this.promotorMapperApi = promotorMapperApi;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<PaginacionResponseDto<PromotorResponse>> listarPromotores(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        String idAdminCreador = jwt.getSubject();
        int finalSize = Math.min(size, 10);
        PaginacionResponseDto<Promotor> paginaCruda =
                listarPromotorUseCase.listarPorAdmin( idAdminCreador,page, finalSize);
        List<PromotorResponse> contenidoSimple =
                promotorMapperApi.promotorToPromotorResponse(paginaCruda.content());
        return ResponseEntity.ok(promotorMapperApi.toSimplePaginacionDto(paginaCruda));
    }
}
