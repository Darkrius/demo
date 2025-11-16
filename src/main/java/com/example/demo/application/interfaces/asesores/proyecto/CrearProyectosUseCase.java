package com.example.demo.application.interfaces.asesores.proyecto;

import com.example.demo.domain.entities.Proyecto;

import java.util.List;

public interface CrearProyectosUseCase {
    List<Proyecto> crearTodos(List<Proyecto> proyectos);
}
