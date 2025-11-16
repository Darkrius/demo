package com.example.demo.application.interfaces.asesores.inmobiliaria;

import com.example.demo.domain.entities.Inmobiliarias;

import java.util.Optional;

public interface BuscarInmobiliariaRucUseCase {
    Optional<Inmobiliarias> buscarPorRuc(String ruc);

}
