package com.example.demo.infraestructure.persistence.mapper;

import com.example.demo.domain.entities.Inmobiliarias;
import com.example.demo.application.dto.query.DashBoardInmobiliaria;
import com.example.demo.infraestructure.persistence.entities.InmobiliariasEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InmobiliariaMapper {

    InmobiliariasEntity toEntity(Inmobiliarias inmobiliaria);

    Inmobiliarias toDomain(InmobiliariasEntity inmobiliariaEntity);

    List<DashBoardInmobiliaria> toDomainList(List<InmobiliariasEntity> entities);


}
