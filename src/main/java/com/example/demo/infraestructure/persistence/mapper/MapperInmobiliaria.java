package com.example.demo.infraestructure.persistence.mapper;

import com.example.demo.domain.model.Inmobiliarias;
import com.example.demo.domain.model.Proyectos;
import com.example.demo.infraestructure.persistence.entities.InmobiliariasEntity;
import com.example.demo.infraestructure.persistence.entities.ProyectosEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapperInmobiliaria {

    InmobiliariasEntity toEntity (Inmobiliarias inmobiliarias);

    Inmobiliarias toModel (InmobiliariasEntity inmobiliariasEntity);

    @Mapping(target = "idInmobiliaria", ignore = true)
    ProyectosEntity toEntity(Proyectos proyectos);
}
