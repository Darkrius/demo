package com.example.demo.infraestructure.persistence.mapper;


import com.example.demo.domain.entities.Asesores;
import com.example.demo.infraestructure.persistence.entities.AsesoresEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AsesorInternoMapper {

    Asesores toDomain(AsesoresEntity asesoresEntity);

    AsesoresEntity toEntity(Asesores asesores);


}
