package com.example.demo.application.interfaces.asesores.inmobiliaria;

import com.example.demo.domain.entities.Inmobiliarias;

import java.util.List;

public interface ListarInmobiliariaUseCase {


    List<Inmobiliarias> listarPorAdmin(String idAdminCreador, int page, int size);
}
