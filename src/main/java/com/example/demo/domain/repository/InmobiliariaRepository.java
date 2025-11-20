package com.example.demo.domain.repository;

import com.example.demo.domain.entities.Inmobiliarias;

import java.util.List;
import java.util.Optional;

public interface InmobiliariaRepository {

    Inmobiliarias crear (Inmobiliarias inmobiliaria);

    Optional<Inmobiliarias> buscarPorRUc(String ruc);

    long contarPorAdmin (String idAdminCreador);

    boolean existePorId(Long idInmobiliaria);

    List<DashBoardInmobiliaria> listarPorAdmin(String idAdminCreador, int page, int size);

    Optional<Inmobiliarias> actualizar(Inmobiliarias inmobiliaria);

    List<Inmobiliarias> listarInmobiliariasClient(String idAdminCreador);

}
