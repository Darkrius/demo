package com.example.demo.domain.repository.command;

import com.example.demo.domain.entities.Asesores;

import java.util.List;


public interface AsesorRepositoryCommand {


    void saveAll(List<Asesores> asesores);

}
