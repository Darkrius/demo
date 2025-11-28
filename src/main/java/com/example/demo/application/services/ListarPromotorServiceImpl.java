package com.example.demo.application.services;

import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.PromotorDashBoardDto;
import com.example.demo.application.interfaces.external.PromotorPortService;
import com.example.demo.application.interfaces.usecases.ListarPromotorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ListarPromotorServiceImpl implements ListarPromotorService {

    private final PromotorPortService promotorPortService;

    public ListarPromotorServiceImpl(PromotorPortService promotorPortService) {
        this.promotorPortService = promotorPortService;
    }

    @Override
    public PaginationResponseDTO<PromotorDashBoardDto> listarPromotor(String idAdminCreador, int page, int size) {
        log.info("SERVICE: Listando Promotores. Admin: [{}], Page: [{}]", idAdminCreador, page);
        return promotorPortService.promotorListar(idAdminCreador, page, size);
    }
}
