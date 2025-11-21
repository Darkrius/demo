package com.example.demo.application.interfaces.asesores.promotor;

import com.example.demo.application.dto.query.PaginacionResponseDto;
import com.example.demo.application.dto.query.PromotorDashBoard;

public interface ListarPromotorUseCase {

    PaginacionResponseDto<PromotorDashBoard> listarPorAdmin(String idAdminCreador, int page, int size);

}
