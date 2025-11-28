package com.example.demo.application.interfaces.usecases;

import com.example.demo.application.dto.commands.RegistrarPromotorCommand;
import com.example.demo.application.dto.queries.PromotorDashBoardDto;

public interface RegistrarPromotorService {


    PromotorDashBoardDto registrar(RegistrarPromotorCommand registrarPromotorCommand, String idAdminCreador);
}
