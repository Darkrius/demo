package com.example.demo.api.mapper;


import com.example.demo.api.dto.CrearInmobiliariaRequest;
import com.example.demo.api.dto.InmobiliariaResponse;
import com.example.demo.api.dto.ProyectoRequestDto;
import com.example.demo.application.interfaces.asesores.inmobiliaria.CreateInmobiliariaCommand;
import com.example.demo.application.interfaces.asesores.proyecto.CreateProyectoCommand;
import com.example.demo.domain.entities.Inmobiliarias;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InmobiliariaMapperApi {


    @Mapping(target = "razonSocial", source = "razonSocialSunat")
    CreateInmobiliariaCommand toCreateInmobiliariaCommand(CrearInmobiliariaRequest request,String razonSocialSunat);

    CreateProyectoCommand proyectoRequestToCommand(ProyectoRequestDto proyectoDto);

    @Mapping(target = "nProyectos", source = "conteoProyectos")
    InmobiliariaResponse toResponseDto(Inmobiliarias inmobiliaria, int conteoProyectos);

    InmobiliariaResponse toResponseDto(Inmobiliarias inmobiliaria);

    List<InmobiliariaResponse> toResponseDtoList(List<Inmobiliarias> inmobiliarias);
}
