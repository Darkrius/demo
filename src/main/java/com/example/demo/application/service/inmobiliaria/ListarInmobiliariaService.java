package com.example.demo.application.service.inmobiliaria;

import com.example.demo.application.dto.PaginacionResponseDto;
import com.example.demo.application.interfaces.asesores.inmobiliaria.ListarInmobiliariaUseCase;
import com.example.demo.domain.repository.DashBoardInmobiliaria;
import com.example.demo.domain.repository.InmobiliariaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarInmobiliariaService implements ListarInmobiliariaUseCase {


    private final InmobiliariaRepository inmobiliariaRepository;

    public ListarInmobiliariaService(InmobiliariaRepository inmobiliariaRepository) {
        this.inmobiliariaRepository = inmobiliariaRepository;
    }

    @Override
    public PaginacionResponseDto<DashBoardInmobiliaria> listarPorAdmin(String idAdminCreador, int page, int size) {
        long totalItems = inmobiliariaRepository.contarPorAdmin(idAdminCreador);
        if (totalItems == 0) {
            return new PaginacionResponseDto<>(List.of(), page, 0, 0);
        }
        List<DashBoardInmobiliaria> inmobiliarias = inmobiliariaRepository.listarPorAdmin(idAdminCreador, page, size);
        int totalPages = (int) Math.ceil((double) totalItems / size);
        return new PaginacionResponseDto<>(inmobiliarias, page, totalPages, totalItems);
    }
}
