package com.example.demo.application.interfaces.usecases;

import com.example.demo.application.dto.commands.RegistrarInmobiliariaCommand;
import com.example.demo.application.dto.queries.InmobiliariaDashBoardDto;

public interface CrearInmobiliariaService {

    InmobiliariaDashBoardDto crearInmobiliaria(RegistrarInmobiliariaCommand command, String idAdminCreador);
}
