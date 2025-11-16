package com.example.demo.application.interfaces.asesores.inmobiliaria;

import com.example.demo.domain.entities.Inmobiliarias;

public interface CrearInmobiliariaUseCase {

    Inmobiliarias crearInmobiliaria(CreateInmobiliariaCommand command, String idAdminCreador);
}
