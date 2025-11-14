package com.example.demo.api.mapper;


import com.example.demo.api.dto.AsesorRequestDto;
import com.example.demo.api.dto.EnriquecerAsesorRequestDto;
import com.example.demo.application.interfaces.asesores.AsesorCommand;
import com.example.demo.application.interfaces.asesores.EnriquecerAsesorCommand;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AsesorMapperApi {

    AsesorCommand toCommand(AsesorRequestDto dto);

    default EnriquecerAsesorCommand toCommand(EnriquecerAsesorRequestDto dto) {
        List<AsesorCommand> list = dto.asesores().stream().map(this::toCommand).toList();
        return new EnriquecerAsesorCommand(list);
    }

}
