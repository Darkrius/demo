package com.example.demo.application.service.promotor;

import com.example.demo.application.dto.query.PaginacionResponseDto;
import com.example.demo.application.interfaces.asesores.PromotorDashboardQueryPort;
import com.example.demo.application.interfaces.asesores.promotor.ListarPromotorUseCase;
import com.example.demo.application.dto.query.PromotorDashBoard;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarPromotoresService implements ListarPromotorUseCase {

    private final PromotorDashboardQueryPort queryPort;

    public ListarPromotoresService(PromotorDashboardQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Override
    public PaginacionResponseDto<PromotorDashBoard> listarPorAdmin(String idAdminCreador, int page, int size) {
        int normalizedPage = Math.max(0, page);
        int normalizedSize = size <= 0 ? 20 : size;


        long totalItems = queryPort.contarPorAdmin(idAdminCreador);
        if (totalItems == 0) {
            return new PaginacionResponseDto<>(List.of(), page, 0, 0);
        }

        int totalPages = (int) ((totalItems + normalizedSize - 1) / normalizedSize);

        if (normalizedPage >= totalPages) {
            return new PaginacionResponseDto<>(List.of(), normalizedPage, totalPages, totalItems);
        }
        List<PromotorDashBoard> inmobiliarias = queryPort.listarPorAdmin(idAdminCreador, page, size);
        return new PaginacionResponseDto<>(inmobiliarias, page, totalPages, totalItems);
    }

    }


