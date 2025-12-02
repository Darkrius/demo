package com.example.demo.application.interfaces.external;

import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.PromotorDashBoardDto;
import com.example.demo.application.dto.queries.PromotorDetalleDto;

import java.util.Optional;

public interface PromotorPortService {

    PaginationResponseDTO<PromotorDashBoardDto> promotorListar(String idAdminCreador, int page, int size);

    Optional<PromotorDetalleDto> listarPromotorPorId(Long idUsuario);
}
