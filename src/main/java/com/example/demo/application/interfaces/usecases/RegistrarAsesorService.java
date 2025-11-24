package com.example.demo.application.interfaces.usecases;

import com.example.demo.application.dto.commands.RegistrarAsesorCommand;
import com.example.demo.application.dto.queries.AsesorDashBoardDto;

public interface RegistrarAsesorService {

    AsesorDashBoardDto registrarAsesor(RegistrarAsesorCommand command);

}
