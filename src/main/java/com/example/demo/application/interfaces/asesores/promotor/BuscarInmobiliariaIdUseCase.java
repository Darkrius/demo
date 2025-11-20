package com.example.demo.application.interfaces.asesores.promotor;

import com.example.demo.domain.entities.Promotor;

import java.util.Optional;

public interface BuscarInmobiliariaIdUseCase {

    Optional<Promotor> buscarPorIdPromotor(Long idUsuario);
}
