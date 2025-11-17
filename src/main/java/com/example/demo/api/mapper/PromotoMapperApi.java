package com.example.demo.api.mapper;

import com.example.demo.api.dto.PromotorResponse;
import com.example.demo.application.dto.PaginacionResponseDto;
import com.example.demo.domain.entities.Promotor;

import java.util.List;

public interface PromotoMapperApi {

    List<PromotorResponse> promotorToPromotorResponse(List<Promotor> promotor);

    default PaginacionResponseDto<PromotorResponse> toSimplePaginacionDto(
            PaginacionResponseDto<Promotor> paginaCruda) {
        if (paginaCruda == null) {
            return null;
        }
        List<PromotorResponse> contenidoSimple = promotorToPromotorResponse(paginaCruda.content());
        return new PaginacionResponseDto<>(
                contenidoSimple,
                paginaCruda.currentPage(),
                paginaCruda.totalPages(),
                paginaCruda.totalItems()
        );
    }


}
