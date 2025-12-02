package com.example.demo.application.services;

import com.example.demo.application.dto.queries.PromotorDetalleDto;
import com.example.demo.application.exceptions.RecursoNoEncontradoException;
import com.example.demo.application.interfaces.external.PromotorPortService;
import com.example.demo.application.interfaces.usecases.DetallePromotorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DetallePromotorServiceImpl implements DetallePromotorService {

    private final PromotorPortService   promotorPortService;

    public DetallePromotorServiceImpl(PromotorPortService promotorPortService) {
        this.promotorPortService = promotorPortService;
    }

    @Override
    public PromotorDetalleDto listar(Long idUsuario) {
        log.info("SERVICE: Listando Promotores. idUsuario: [{}]",idUsuario);
       return promotorPortService.listarPromotorPorId(idUsuario)
               .orElseThrow(() -> new RecursoNoEncontradoException("No existe un promotor con el id"));
    }
}
