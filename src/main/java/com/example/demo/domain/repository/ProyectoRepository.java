package com.example.demo.domain.repository;

import com.example.demo.domain.entities.Proyecto;

import java.util.List;
import java.util.Optional;

public interface ProyectoRepository {

    
    List<Proyecto> crearTodos(List<Proyecto> proyectos);

    long contarPorInmobiliaria(Long idInmobiliaria);

    Optional<Proyecto> buscarPorId(Long idProyecto);
}
