package com.example.demo.application.interfaces.usecases;

import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.AsesorDashBoardDto;

public interface ListarAsesorService {

    PaginationResponseDTO<AsesorDashBoardDto> listarAsesoresGestion(int page, int size, String nombre);

}
