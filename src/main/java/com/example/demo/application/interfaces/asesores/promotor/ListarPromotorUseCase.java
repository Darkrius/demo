package com.example.demo.application.interfaces.asesores.promotor;

import com.example.demo.application.dto.PaginacionResponseDto;
import com.example.demo.domain.entities.Promotor;

public interface ListarPromotorUseCase {

    PaginacionResponseDto<Promotor> listarPorAdmin(String idAdminCreador, int page, int size);

}
