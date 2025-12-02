package com.example.demo.application.services;

import com.example.demo.application.dto.queries.InmobiliariaDetalleDto;
import com.example.demo.application.exceptions.RecursoNoEncontradoException;
import com.example.demo.application.interfaces.external.InmobiliariaPortService;
import com.example.demo.application.interfaces.usecases.DetalleInmobiliariaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class DetalleInmobiliariaServiceImpl implements DetalleInmobiliariaService {

    private final InmobiliariaPortService inmobiliariaPortService;

    public DetalleInmobiliariaServiceImpl(InmobiliariaPortService inmobiliariaPortService) {
        this.inmobiliariaPortService = inmobiliariaPortService;
    }

    @Override
    public InmobiliariaDetalleDto listar(Long idInmobiliaria) {
        log.info("SERVICE: Listando Promotores. idInmobiliaria: [{}]",idInmobiliaria);
        return inmobiliariaPortService.listarInmobiliariaPorId(idInmobiliaria)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una inmobiliaria con el id"));
    }
}
