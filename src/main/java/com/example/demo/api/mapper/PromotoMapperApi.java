package com.example.demo.api.mapper;

import com.example.demo.api.dto.CrearPromotorRequest;
import com.example.demo.api.dto.PromotorResponse;
import com.example.demo.application.dto.query.PaginacionResponseDto;
import com.example.demo.application.interfaces.asesores.promotor.CrearPromotorCommand;
import com.example.demo.domain.entities.Promotor;
import com.example.demo.application.dto.query.PromotorDashBoard;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PromotoMapperApi {



    PromotorResponse promotorToPromotorResponse(Promotor promotor);


    CrearPromotorCommand crearPromotorRequestToCrearPromotorCommand(CrearPromotorRequest request);

    PromotorResponse promotorDashBoardToPromotorResponse(PromotorDashBoard promotorDashBoard);

    List<PromotorResponse> promotorDashBoardToPromotorResponse(List<PromotorDashBoard> promotorDashBoards);


    default PaginacionResponseDto<PromotorResponse> toSimplePaginacionDtoFromDashBoard(
            PaginacionResponseDto<PromotorDashBoard> paginaCruda) {
        if (paginaCruda == null) {
            return null;
        }
        List<PromotorResponse> contenidoSimple = promotorDashBoardToPromotorResponse(paginaCruda.content());
        return new PaginacionResponseDto<>(
                contenidoSimple,
                paginaCruda.currentPage(),
                paginaCruda.totalPages(),
                paginaCruda.totalItems()
        );
    }
}
