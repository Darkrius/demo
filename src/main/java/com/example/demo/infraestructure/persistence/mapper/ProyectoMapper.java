package com.example.demo.infraestructure.persistence.mapper;

import com.example.demo.domain.entities.Proyecto;
import com.example.demo.infraestructure.persistence.entities.ProyectosEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProyectoMapper {

    ProyectosEntity toEntity(Proyecto proyecto);

    Proyecto toDomain(ProyectosEntity proyectoEntity);

    List<Proyecto> toDomainList(List<ProyectosEntity> entities);


}
