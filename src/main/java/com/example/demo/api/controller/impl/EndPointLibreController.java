package com.example.demo.api.controller.impl;

import com.example.demo.api.controller.interfaces.EndPointLibreApi;
import com.example.demo.application.dto.SelectorDto;
import com.example.demo.application.interfaces.usecases.SelectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class EndPointLibreController implements EndPointLibreApi {

    private final SelectorService selectorService;

    public EndPointLibreController(SelectorService selectorService) {
        this.selectorService = selectorService;
    }




    @Override
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<SelectorDto>> listarInmobiliaria(Jwt jwt) {
        String idAdmin = jwt.getSubject();
        log.info("API: Solicitando selector de inmobiliarias. Admin: [{}]", idAdmin);
        return ResponseEntity.ok(selectorService.obtenerInmobiliaria(idAdmin));
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<SelectorDto>> getSelectorProyectos(Long idInmobiliaria) {
        log.info("API: Solicitando selector de proyectos. IdInmobiliaria [{}]", idInmobiliaria);
        return ResponseEntity.ok(selectorService.obtenerProyecto(idInmobiliaria));

    }
}
