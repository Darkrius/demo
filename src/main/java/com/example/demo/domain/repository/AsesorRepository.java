package com.example.demo.domain.repository;

import com.example.demo.domain.dto.AsesorDashBoard;
import com.example.demo.domain.model.Asesores;

import java.util.List;
import java.util.Optional;

public interface AsesorRepository {

    void guardar(Asesores asesor);


    List<AsesorDashBoard> buscarPorNombreParcial(String nombre, int page, int size);


    Optional<String> buscarIdUbigeoPorCiudad(String ciudad);
}
