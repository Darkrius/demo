package com.example.demo.application.interfaces.external;

import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.PromotorDashBoardDto;

public interface PromotorPortService {

    PaginationResponseDTO<PromotorDashBoardDto> promotorListar(String idAdminCreador, int page, int size);

}
