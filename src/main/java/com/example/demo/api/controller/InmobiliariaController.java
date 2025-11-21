package com.example.demo.api.controller;

import com.example.demo.api.dto.CrearInmobiliariaRequest;
import com.example.demo.api.dto.InmobiliariaLookupDtoResponse;
import com.example.demo.api.dto.InmobiliariaResponse;
import com.example.demo.api.mapper.InmobiliariaMapperApi;
import com.example.demo.application.dto.query.PaginacionResponseDto;
import com.example.demo.application.interfaces.asesores.inmobiliaria.CrearInmobiliariaUseCase;
import com.example.demo.application.interfaces.asesores.inmobiliaria.CreateInmobiliariaCommand;
import com.example.demo.application.interfaces.asesores.inmobiliaria.ListarInmobiliariaUseCase;
import com.example.demo.application.interfaces.asesores.inmobiliaria.ListarInmobiliariasClientUseCase;
import com.example.demo.domain.entities.Inmobiliarias;
import com.example.demo.application.dto.query.DashBoardInmobiliaria;
import com.example.demo.domain.repository.SunatPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inmobiliarias")
public class InmobiliariaController {

    private final CrearInmobiliariaUseCase crearInmobiliariaUseCase;
    private final ListarInmobiliariaUseCase listarInmobiliariaUseCase;
    private final InmobiliariaMapperApi inmobiliariaMapperApi;
    private final SunatPort sunatPort;
    private final ListarInmobiliariasClientUseCase listarInmobiliariasClientUseCase;


    public InmobiliariaController(CrearInmobiliariaUseCase crearInmobiliariaUseCase, ListarInmobiliariaUseCase listarInmobiliariaUseCase, InmobiliariaMapperApi inmobiliariaMapperApi, SunatPort sunatPort, ListarInmobiliariasClientUseCase listarInmobiliariasClientUseCase) {
        this.crearInmobiliariaUseCase = crearInmobiliariaUseCase;
        this.listarInmobiliariaUseCase = listarInmobiliariaUseCase;
        this.inmobiliariaMapperApi = inmobiliariaMapperApi;
        this.sunatPort = sunatPort;
        this.listarInmobiliariasClientUseCase = listarInmobiliariasClientUseCase;
    }


    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<InmobiliariaResponse> crearInmobiliaria(
            @Valid @RequestBody CrearInmobiliariaRequest requestDto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String idAdminCreador = jwt.getSubject();
        String razonSocial = sunatPort.obtenerRazonSocialPorRuc(requestDto.ruc())
                .orElseThrow(() -> new RuntimeException("El RUC " + requestDto.ruc() + " no existe en SUNAT."));
        CreateInmobiliariaCommand command = inmobiliariaMapperApi.toCreateInmobiliariaCommand(requestDto, razonSocial);
        Inmobiliarias inmobiliariaGuardada = crearInmobiliariaUseCase.crearInmobiliaria(command, idAdminCreador);
        int nProyectos = (command.proyectos() != null) ? command.proyectos().size() : 0;
        InmobiliariaResponse responseDto = inmobiliariaMapperApi.toResponseDto(inmobiliariaGuardada, nProyectos);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<PaginacionResponseDto<InmobiliariaResponse>> listarInmobiliarias(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        String idAdminCreador = jwt.getSubject();
        int finalSize = Math.min(size, 10);

        PaginacionResponseDto<DashBoardInmobiliaria> paginaDomain =
                listarInmobiliariaUseCase.listarPorAdmin(idAdminCreador, page, finalSize);

        PaginacionResponseDto<InmobiliariaResponse> response =
                inmobiliariaMapperApi.toPagedResponse(paginaDomain);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/client")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<InmobiliariaLookupDtoResponse>> listarInmobiliariasClient(
            @AuthenticationPrincipal Jwt jwt
    ) {
        String idAdminCreador = jwt.getSubject();
        List<Inmobiliarias> inmobiliarias = listarInmobiliariasClientUseCase.execute(idAdminCreador);
        List<InmobiliariaLookupDtoResponse> dtos = inmobiliarias.stream()
                .map(inmobiliariaMapperApi::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

}
