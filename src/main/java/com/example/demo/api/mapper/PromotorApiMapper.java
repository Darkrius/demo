package com.example.demo.api.mapper;


import com.example.demo.api.dto.request.PromotorRequest;
import com.example.demo.application.dto.commands.RegistrarPromotorCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromotorApiMapper {


    // no mapeamos se llaman iguales xd
    RegistrarPromotorCommand toCommand(PromotorRequest request);
}
