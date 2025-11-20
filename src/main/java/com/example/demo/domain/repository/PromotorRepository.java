package com.example.demo.domain.repository;

import com.example.demo.domain.entities.Promotor;

import java.util.List;
import java.util.Optional;

public interface PromotorRepository {

    Promotor crear (Promotor promotor);

    Optional<Promotor> buscarPorId(Long idUsuario);

    Optional<Promotor> actualizar(Promotor promotor);

    long contarPorAdmin (String idAdminCreador);

    List<Promotor> listarPorAdmin(String idAdminCreador, int page, int size);
}
