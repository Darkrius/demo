package com.example.demo.domain.repository.query;

import com.example.demo.domain.entities.AsesorExterno;
import com.example.demo.domain.entities.Asesores;

import java.util.List;

public interface AsesorRepositoryQuery {

    List<AsesorExterno> listarAsesores(String nombre, String ciudad, int page, int size);
    long contarAsesores(String nombre, String ciudad);

}
