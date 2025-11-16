package com.example.demo.application.interfaces.asesores.proyecto;

import com.example.demo.domain.entities.Proyecto;

import java.util.Optional;

public interface BuscarProyectoUseCase {

    Optional<Proyecto> buscarPorId(Long idProyecto);
}
