package com.example.demo.application.interfaces.usecases;

import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.PromotorDashBoardDto;

public interface ListarPromotorService {

    PaginationResponseDTO<PromotorDashBoardDto> listarPromotor(String idAdminCreador, int page, int size);
}
