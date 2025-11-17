package com.example.demo.application.interfaces.asesores.inmobiliaria;

import com.example.demo.application.dto.PaginacionResponseDto;
import com.example.demo.domain.repository.DashBoardInmobiliaria;

public interface ListarInmobiliariaUseCase {


    PaginacionResponseDto<DashBoardInmobiliaria> listarPorAdmin(String idAdminCreador, int page, int size);
}
