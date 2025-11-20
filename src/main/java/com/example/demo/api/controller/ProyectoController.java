package com.example.demo.api.controller;


import com.example.demo.api.dto.ProyectoLookupDtoResponse;
import com.example.demo.api.mapper.ProyectoMapperApi;
import com.example.demo.application.interfaces.asesores.proyecto.GetProyectosLookupUseCase;
import com.example.demo.domain.entities.Proyecto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final GetProyectosLookupUseCase getProyectosLookupUseCase;
    private final ProyectoMapperApi proyectoMapperApi;


    public ProyectoController(GetProyectosLookupUseCase getProyectosLookupUseCase, ProyectoMapperApi proyectoMapperApi) {
        this.getProyectosLookupUseCase = getProyectosLookupUseCase;
        this.proyectoMapperApi = proyectoMapperApi;
    }

    @GetMapping("/lookup")
    public ResponseEntity<List<ProyectoLookupDtoResponse>> listarProyectosLookup(@RequestParam Long idInmobiliaria) {
        List<Proyecto> proyectos = getProyectosLookupUseCase.execute(idInmobiliaria);
        List<ProyectoLookupDtoResponse> dtos = proyectos.stream()
                .map(proyectoMapperApi::toDtoProyecto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
