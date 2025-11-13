package com.example.demo.application.service;

import com.example.demo.application.interfaces.asesores.AsesorCreateUseCase;
import com.example.demo.domain.entities.Asesores;
import com.example.demo.domain.repository.command.AsesorRepositoryCommand;
import org.springframework.stereotype.Service;

//@Service
//public class AsesorCreateService implements AsesorCreateUseCase {
//
//
//    private final AsesorRepositoryCommand asesorRepositoryCommand;
//
//    public AsesorCreateService(AsesorRepositoryCommand asesorRepositoryCommand) {
//        this.asesorRepositoryCommand = asesorRepositoryCommand;
//    }
//
//    @Override
//    public Asesores create(Asesores asesores) {
//        return asesorRepositoryCommand.save(asesores);
//    }
//}
