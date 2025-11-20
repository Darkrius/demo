package com.example.demo.infraestructure.persistence.mapper;


import com.example.demo.domain.entities.Promotor;
import com.example.demo.infraestructure.persistence.entities.PromotorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PromotorMapper {

    PromotorEntity toEntity(Promotor promotor);

    Promotor toDomain(PromotorEntity promotorEntity);

    List<Promotor> toDomainList(List<PromotorEntity> entities);


}
