package com.example.demo.application.services;

import com.example.demo.application.dto.queries.InmobiliariaDetalleDto;
import com.example.demo.application.exceptions.RecursoNoEncontradoException;
import com.example.demo.application.exceptions.ReglasNegocioException;
import com.example.demo.application.interfaces.external.InmobiliariaPortService;
import com.example.demo.application.interfaces.usecases.EditarInmobiliariaService;
import com.example.demo.domain.dto.EditarInmobiliaria;
import com.example.demo.domain.model.Inmobiliarias;
import com.example.demo.domain.repository.InmobilariaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class EditarInmobiliariaServiceImpl implements EditarInmobiliariaService {

    private final InmobilariaRepository inmobilariaRepository;
    private final InmobiliariaPortService inmobiliariaPortService;


    public EditarInmobiliariaServiceImpl(InmobilariaRepository inmobilariaRepository, InmobiliariaPortService inmobiliariaPortService) {
        this.inmobilariaRepository = inmobilariaRepository;
        this.inmobiliariaPortService = inmobiliariaPortService;
    }

    @Override
    public InmobiliariaDetalleDto editar(EditarInmobiliaria command) {
        log.info("SERVICE: Editando inmobiliaria. ID: [{}]", command.idInmobiliaria());

        // 1. Guardar (Command)
        inmobilariaRepository.guardarEdicion(command);


        // 2. Recuperar (Query) - Ahora el tipo coincide
        return inmobiliariaPortService.listarInmobiliariaPorId(command.idInmobiliaria())
                .orElseThrow(() -> {
                    log.error("SERVICE ERROR: Inmobiliaria ID [{}] no encontrada tras edición.", command.idInmobiliaria());
                    return new RecursoNoEncontradoException("No se pudo recuperar la inmobiliaria tras la edición.");
                });
    }
}
