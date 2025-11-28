package com.example.demo.infraestructure.persistence.mapper;


import com.example.demo.domain.model.Promotor;
import com.example.demo.infraestructure.persistence.entities.PromotorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapperPromotor {

    PromotorEntity toEntity (Promotor promotor);

    @Mapping(target = "proyectosAsignados", ignore = true)
    Promotor toDomain (PromotorEntity promotorEntity);
}
