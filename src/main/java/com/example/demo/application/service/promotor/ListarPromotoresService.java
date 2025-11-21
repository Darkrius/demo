package com.example.demo.application.service.promotor;

import com.example.demo.application.dto.query.DashBoardInmobiliaria;
import com.example.demo.application.dto.query.PaginacionResponseDto;
import com.example.demo.application.interfaces.asesores.PromotorDashboardQueryPort;
import com.example.demo.application.interfaces.asesores.promotor.ListarPromotorUseCase;
import com.example.demo.application.dto.query.PromotorDashBoard;
import com.example.demo.domain.entities.Promotor;
import com.example.demo.domain.repository.PromotorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListarPromotoresService implements ListarPromotorUseCase {

    private final PromotorDashboardQueryPort queryPort;

    public ListarPromotoresService(PromotorDashboardQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @Override
    public PaginacionResponseDto<DashBoardInmobiliaria> listarPorAdmin(String idAdminCreador, int page, int size) {

    }
}

