package com.example.demo.api.mapper;


import com.example.demo.api.dto.AsesorSimpleResponseDto;
import com.example.demo.application.dto.PaginacionResponseDto;
import com.example.demo.domain.entities.AsesorExterno;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AsesorApiMapper {

    AsesorSimpleResponseDto toSimpleDto(AsesorExterno asesor);

    List<AsesorSimpleResponseDto> toSimpleDtoList(List<AsesorExterno> asesores);

    default PaginacionResponseDto<AsesorSimpleResponseDto> toSimplePaginacionDto(
            PaginacionResponseDto<AsesorExterno> paginaCruda) {
        if (paginaCruda == null) {
            return null;
        }
        List<AsesorSimpleResponseDto> contenidoSimple = toSimpleDtoList(paginaCruda.content());
        return new PaginacionResponseDto<>(
                contenidoSimple,
                paginaCruda.currentPage(),
                paginaCruda.totalPages(),
                paginaCruda.totalItems()
        );
    }
}
