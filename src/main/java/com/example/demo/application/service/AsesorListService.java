package com.example.demo.application.service;

import com.example.demo.application.dto.PaginacionResponseDto;
import com.example.demo.application.interfaces.asesores.ListarAsesorUseCase;
import com.example.demo.domain.entities.AsesorExterno;
import com.example.demo.domain.repository.query.AsesorRepositoryQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsesorListService implements ListarAsesorUseCase {

    private final AsesorRepositoryQuery asesorRepositoryQuery;

    public AsesorListService(AsesorRepositoryQuery asesorRepositoryQuery) {
        this.asesorRepositoryQuery = asesorRepositoryQuery;
    }

    @Override
    public PaginacionResponseDto<AsesorExterno> listarAsesoresPage(String nombre, String ciudad, int page, int size) {
        long totalItems = asesorRepositoryQuery.contarAsesores(nombre, ciudad);
        if (totalItems == 0) {
            return new PaginacionResponseDto<>(List.of(), page, 0, 0);
        }
        List<AsesorExterno> asesores = asesorRepositoryQuery.listarAsesores(nombre, ciudad, page, size);
        int totalPages = (int) Math.ceil((double) totalItems / size);
        return new PaginacionResponseDto<>(asesores, page, totalPages, totalItems);
    }
}
