package com.example.demo.api.controller;

import com.example.demo.api.dto.CrearInmobiliariaRequest;
import com.example.demo.api.dto.InmobiliariaResponse;
import com.example.demo.api.mapper.InmobiliariaMapperApi;
import com.example.demo.application.interfaces.asesores.inmobiliaria.CrearInmobiliariaUseCase;
import com.example.demo.application.interfaces.asesores.inmobiliaria.CreateInmobiliariaCommand;
import com.example.demo.domain.entities.Inmobiliarias;
import com.example.demo.domain.repository.SunatPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inmobiliarias")
public class InmobiliariaController {

    private final CrearInmobiliariaUseCase crearInmobiliariaUseCase;
    private final InmobiliariaMapperApi inmobiliariaMapperApi;
    private final SunatPort sunatPort;


    public InmobiliariaController(CrearInmobiliariaUseCase crearInmobiliariaUseCase, InmobiliariaMapperApi inmobiliariaMapperApi, SunatPort sunatPort) {
        this.crearInmobiliariaUseCase = crearInmobiliariaUseCase;
        this.inmobiliariaMapperApi = inmobiliariaMapperApi;
        this.sunatPort = sunatPort;
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
}
