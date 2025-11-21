package com.example.demo.application.interfaces.asesores.inmobiliaria;

import com.example.demo.application.dto.query.PaginacionResponseDto;
import com.example.demo.application.dto.query.DashBoardInmobiliaria;

public interface ListarInmobiliariaUseCase {


    PaginacionResponseDto<DashBoardInmobiliaria> listarPorAdmin(String idAdminCreador, int page, int size);
}
