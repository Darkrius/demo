package com.example.demo.infraestructure.persistence.mapper;

import com.example.demo.domain.model.Asesores;
import com.example.demo.infraestructure.persistence.entities.AsesoresEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapperAsesor {


    AsesoresEntity toEntity(Asesores asesor);

    Asesores toModel(AsesoresEntity asesorEntity);
}
