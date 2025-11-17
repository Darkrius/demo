package com.example.demo.application.service.promotor;

import com.example.demo.application.dto.PaginacionResponseDto;
import com.example.demo.application.interfaces.asesores.promotor.ListarPromotorUseCase;
import com.example.demo.domain.entities.Promotor;
import com.example.demo.domain.repository.PromotorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarPromotoresService implements ListarPromotorUseCase {

    private final PromotorRepository promotorRepository;

    public ListarPromotoresService(PromotorRepository promotorRepository) {
        this.promotorRepository = promotorRepository;
    }

    @Override
    public PaginacionResponseDto<Promotor> listarPorAdmin(String idAdminCreador, int page, int size) {
        long totalItems = promotorRepository.contarPorAdmin(idAdminCreador);
        if (totalItems == 0) {
            return new PaginacionResponseDto<>(List.of(), page, 0, 0);
        }
        List<Promotor> asesores = promotorRepository.listarPorAdmin(idAdminCreador,page, size);
        int totalPages = (int) Math.ceil((double) totalItems / size);
        return new PaginacionResponseDto<>(asesores, page, totalPages, totalItems);
    }
}
