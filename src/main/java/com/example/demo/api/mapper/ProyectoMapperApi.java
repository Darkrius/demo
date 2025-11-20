package com.example.demo.api.mapper;

import com.example.demo.api.dto.ProyectoLookupDtoResponse;
import com.example.demo.domain.entities.Proyecto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProyectoMapperApi {

    ProyectoLookupDtoResponse toDtoProyecto(Proyecto proyecto);

}
