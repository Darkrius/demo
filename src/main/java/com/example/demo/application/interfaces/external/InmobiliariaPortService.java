package com.example.demo.application.interfaces.external;

import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.InmobiliariaDashBoardDto;
import com.example.demo.application.dto.queries.InmobiliariaDetalleDto;

import java.util.Optional;

public interface InmobiliariaPortService {

    PaginationResponseDTO<InmobiliariaDashBoardDto> inmobiliariaListar (int page, int size, String idAdminCreador);

    Optional<InmobiliariaDetalleDto> listarInmobiliariaPorId(Long idInmobiliaria);

}
