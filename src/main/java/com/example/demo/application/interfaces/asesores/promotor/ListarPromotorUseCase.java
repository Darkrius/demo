package com.example.demo.application.interfaces.asesores.promotor;

import com.example.demo.application.dto.query.DashBoardInmobiliaria;
import com.example.demo.application.dto.query.PaginacionResponseDto;
import com.example.demo.application.dto.query.PromotorDashBoard;

public interface ListarPromotorUseCase {

    PaginacionResponseDto<DashBoardInmobiliaria> listarPorAdmin(String idAdminCreador, int page, int size);

}
