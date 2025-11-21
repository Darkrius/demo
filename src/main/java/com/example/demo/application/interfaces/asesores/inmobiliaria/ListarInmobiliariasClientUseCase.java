package com.example.demo.application.interfaces.asesores.inmobiliaria;

import com.example.demo.domain.entities.Inmobiliarias;

import java.util.List;

public interface ListarInmobiliariasClientUseCase {

    List<Inmobiliarias> execute (String idAdminCreador);
}
