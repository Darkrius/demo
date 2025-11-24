package com.example.demo.application.services;

import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.AsesorLegacyDto;
import com.example.demo.application.interfaces.external.AsesorLegacyService;
import com.example.demo.application.interfaces.usecases.ListarAsesoresExternosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ListarAsesoresExternosServiceImpl implements ListarAsesoresExternosService {

    private final AsesorLegacyService asesorLegacyService;

    public ListarAsesoresExternosServiceImpl(AsesorLegacyService asesorLegacyService) {
        this.asesorLegacyService = asesorLegacyService;
    }

    @Override
    public PaginationResponseDTO<AsesorLegacyDto> listarCandidatosExternos(int page, int size, String nombre, String ciudad) {
        log.info("SERVICE: Solicitando lista a Sistema Externo (Legacy). Filtros -> Nombre: [{}], Ciudad: [{}]", nombre, ciudad);
        return asesorLegacyService.listar(page, size, nombre, ciudad);
    }
}
