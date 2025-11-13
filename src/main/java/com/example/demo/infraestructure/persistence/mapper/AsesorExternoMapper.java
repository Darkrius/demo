package com.example.demo.infraestructure.persistence.mapper;

import com.example.demo.domain.entities.AsesorExterno;
import com.example.demo.infraestructure.persistence.entities.AsesorExternoEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AsesorExternoMapper {

    AsesorExterno toDomain(AsesorExternoEntity asesorExternoEntity);

    AsesorExternoEntity toEntity(AsesorExterno asesorExterno);

    List<AsesorExterno> toDomainList(List<AsesorExternoEntity> entities);


}
