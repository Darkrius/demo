package com.example.demo.infraestructure.persistence.mapper;

import com.example.demo.domain.entities.Inmobiliarias;
import com.example.demo.infraestructure.persistence.entities.InmobiliariasEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InmobiliariaMapper {

    InmobiliariasEntity toEntity(Inmobiliarias inmobiliaria);

    Inmobiliarias toDomain(InmobiliariasEntity inmobiliariaEntity);

}
