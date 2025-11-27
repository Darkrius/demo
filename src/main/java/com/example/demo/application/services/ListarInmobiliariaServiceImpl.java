package com.example.demo.application.services;


import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.InmobiliariaDashBoardDto;
import com.example.demo.application.interfaces.external.InmobiliariaPortService;
import com.example.demo.application.interfaces.usecases.ListarInmobiliaService;
import com.example.demo.domain.repository.InmobilariaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



@Service
@Slf4j
public class ListarInmobiliariaServiceImpl implements ListarInmobiliaService {

    private final InmobiliariaPortService inmobiliariaPortService;

    public ListarInmobiliariaServiceImpl(InmobiliariaPortService inmobiliariaPortService) {
        this.inmobiliariaPortService = inmobiliariaPortService;
    }


    @Override
    public PaginationResponseDTO<InmobiliariaDashBoardDto> listarInmobiliaria(String idAdminCreador, int page, int size) {
        log.info("SERVICE: Solicitando lista a Sistema Externo (Legacy). Filtros -> idAdminCreador: [{}], page: [{}], size: [{}]", idAdminCreador, page, size);
        return inmobiliariaPortService.inmobiliariaListar(page, size, idAdminCreador);
    }
}
