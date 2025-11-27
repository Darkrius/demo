package com.example.demo.api.controller.interfaces;


import com.example.demo.application.dto.SelectorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/v1/giovanni")
@Tag(name = "EndPoint para el consumo xd ", description = "EndPoint libres para el llenado de datos")
public interface EndPointLibreApi {


    @GetMapping
    ResponseEntity<List<SelectorDto>> listarInmobiliaria(@AuthenticationPrincipal Jwt jwt);


    @Operation(summary = "Selector de Proyectos", description = "Devuelve proyectos activos de una inmobiliaria específica.")
    @GetMapping("/{idInmobiliaria}/proyectos-selector")
    ResponseEntity<List<SelectorDto>> getSelectorProyectos(
            @Parameter(description = "ID de la Inmobiliaria padre")
            @PathVariable("idInmobiliaria") Long idInmobiliaria);

}

