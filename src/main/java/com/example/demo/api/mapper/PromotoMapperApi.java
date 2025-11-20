package com.example.demo.api.mapper;

import com.example.demo.api.dto.CrearPromotorRequest;
import com.example.demo.api.dto.PromotorResponse;
import com.example.demo.application.dto.PaginacionResponseDto;
import com.example.demo.application.interfaces.asesores.promotor.CrearPromotorCommand;
import com.example.demo.domain.entities.Promotor;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
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

    PromotorResponse promotorToPromotorResponse(Promotor promotor);

    Promotor crearPromotorRequestToPromotor(CrearPromotorRequest request, String idAdminCreador);

    CrearPromotorCommand crearPromotorRequestToCrearPromotorCommand(CrearPromotorRequest request);



}
