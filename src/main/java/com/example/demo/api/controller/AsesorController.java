package com.example.demo.api.controller;


import com.example.demo.api.dto.AsesorSimpleResponseDto;
import com.example.demo.api.dto.EnriquecerAsesorRequestDto;
import com.example.demo.api.mapper.AsesorApiMapper;
import com.example.demo.api.mapper.AsesorMapperApi;
import com.example.demo.application.dto.query.PaginacionResponseDto;
import com.example.demo.application.interfaces.asesores.EnriquecerAsesorCommand;
import com.example.demo.application.interfaces.asesores.EnriquecerAsesorUseCase;
import com.example.demo.application.interfaces.asesores.ListarAsesorUseCase;
import com.example.demo.domain.entities.AsesorExterno;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gestion/asesores-externos")
public class AsesorController {

    private final AsesorApiMapper apiMapper;
    private final ListarAsesorUseCase asesorUseCase;
    private final AsesorMapperApi asesorMapperApi;
    private final EnriquecerAsesorUseCase enriquecerAsesorUseCase;

    public AsesorController(AsesorApiMapper apiMapper, ListarAsesorUseCase asesorUseCase, AsesorMapperApi asesorMapperApi, EnriquecerAsesorUseCase enriquecerAsesorUseCase) {
        this.apiMapper = apiMapper;
        this.asesorUseCase = asesorUseCase;
        this.asesorMapperApi = asesorMapperApi;
        this.enriquecerAsesorUseCase = enriquecerAsesorUseCase;
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

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<Void> enriquecerAsesor(@RequestBody EnriquecerAsesorRequestDto requestDto) {
        EnriquecerAsesorCommand command = asesorMapperApi.toCommand(requestDto);
        enriquecerAsesorUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
