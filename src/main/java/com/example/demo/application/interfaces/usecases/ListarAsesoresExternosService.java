package com.example.demo.application.interfaces.usecases;

import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.AsesorLegacyDto;

public interface ListarAsesoresExternosService {

    PaginationResponseDTO<AsesorLegacyDto> listarCandidatosExternos(int page, int size, String nombre , String ciudad);

}
