package com.example.demo.application.service.inmobiliaria;

import com.example.demo.application.interfaces.asesores.inmobiliaria.BuscarInmobiliariaPorUseCase;
import com.example.demo.domain.entities.Inmobiliarias;
import com.example.demo.domain.repository.InmobiliariaRepository;

import java.util.Optional;

public class BuscarInmobiliariaService implements BuscarInmobiliariaPorUseCase {

    private final InmobiliariaRepository inmobiliariaRepository;

    public BuscarInmobiliariaService(InmobiliariaRepository inmobiliariaRepository) {
        this.inmobiliariaRepository = inmobiliariaRepository;
    }


    @Override
    public boolean existePorId(Long idInmobiliaria) {
        return inmobiliariaRepository.existePorId(idInmobiliaria);
    }
}
