package com.example.demo.infraestructure.persistence.mapper;


import com.example.demo.domain.entities.AsesorExterno;
import com.example.demo.domain.entities.Promotor;
import com.example.demo.infraestructure.persistence.entities.AsesorExternoEntity;
import com.example.demo.infraestructure.persistence.entities.PromotorEntity;
import org.mapstruct.MapperConfig;

import java.util.List;

@MapperConfig(componentModel = "spring")
public interface PromotorMapper {

    PromotorEntity toEntity(Promotor promotor);

    Promotor toDomain(PromotorEntity promotorEntity);

    List<Promotor> toDomainList(List<PromotorEntity> entities);


}
