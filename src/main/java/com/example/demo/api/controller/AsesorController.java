package com.example.demo.api.controller;


import com.example.demo.api.dto.AsesorSimpleResponseDto;
import com.example.demo.api.mapper.AsesorApiMapper;
import com.example.demo.application.dto.PaginacionResponseDto;
import com.example.demo.application.interfaces.asesores.ListarAsesorUseCase;
import com.example.demo.domain.entities.AsesorExterno;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/gestion/asesores-externos")
public class AsesorController {

    private final AsesorApiMapper apiMapper;
    private final ListarAsesorUseCase asesorUseCase;

    public AsesorController(AsesorApiMapper apiMapper, ListarAsesorUseCase asesorUseCase) {
        this.apiMapper = apiMapper;
        this.asesorUseCase = asesorUseCase;
    }


    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<PaginacionResponseDto<AsesorSimpleResponseDto>> obtenerAsesoresExternos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String ciudad,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        int finalSize = Math.min(size, 10);
        PaginacionResponseDto<AsesorExterno> paginaCruda =
                asesorUseCase.listarAsesoresPage(nombre, ciudad, page, finalSize);
        List<AsesorSimpleResponseDto> contenidoSimple =
                apiMapper.toSimpleDtoList(paginaCruda.content());
        return ResponseEntity.ok(apiMapper.toSimplePaginacionDto(paginaCruda));
    }
}
