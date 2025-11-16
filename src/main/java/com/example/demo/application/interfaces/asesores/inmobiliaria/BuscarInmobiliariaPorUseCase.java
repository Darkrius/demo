package com.example.demo.application.interfaces.asesores.inmobiliaria;

import com.example.demo.domain.entities.Inmobiliarias;

import java.util.Optional;

public interface BuscarInmobiliariaPorUseCase {

    Optional<Inmobiliarias> buscarPorId(Long idInmobiliaria);
}
