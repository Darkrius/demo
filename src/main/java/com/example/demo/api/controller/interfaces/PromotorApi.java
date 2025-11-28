package com.example.demo.api.controller.interfaces;

import com.example.demo.api.dto.request.PromotorRequest;
import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.PromotorDashBoardDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/promotores")
@Tag(name = "Gestión de Promotores", description = "Registro y administración de los promotores.")
public interface PromotorApi {


    @Operation(summary = "Registrar Promotor", description = "Crea un nuevo promotor y le asigna proyectos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Promotor creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "DOI o Correo duplicado")
    })
    @PostMapping
    ResponseEntity<PromotorDashBoardDto> registrar(
            @RequestBody @Valid PromotorRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt
    );


    @Operation(summary = "Listar Promotores", description = "Obtiene el dashboard paginado.")
    @GetMapping
    ResponseEntity<PaginationResponseDTO<PromotorDashBoardDto>> listar(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt
    );
}
