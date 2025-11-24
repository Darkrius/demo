package com.example.demo.application.interfaces.external;

import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.AsesorLegacyDto;

public interface AsesorLegacyService {
    PaginationResponseDTO<AsesorLegacyDto> listar(
            int page,
            int size,
            String nombre,
            String ciudad
    );
}
