package com.example.demo.application.interfaces.usecases;

import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.InmobiliariaDashBoardDto;

public interface ListarInmobiliaService {

    PaginationResponseDTO<InmobiliariaDashBoardDto> listarInmobiliaria (String idAdminCreador, int page, int size);
}
