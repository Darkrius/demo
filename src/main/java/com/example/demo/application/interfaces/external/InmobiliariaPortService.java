package com.example.demo.application.interfaces.external;

import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.InmobiliariaDashBoardDto;

public interface InmobiliariaPortService {

    PaginationResponseDTO<InmobiliariaDashBoardDto> inmobiliariaListar (int page, int size, String idAdminCreador);

}
