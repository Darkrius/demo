package com.example.demo.api.mapper;

import com.example.demo.api.dto.request.InmobiliariaRequest;
import com.example.demo.application.dto.UploadFileCommand;
import com.example.demo.application.dto.commands.RegistrarInmobiliariaCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Mapper(componentModel = "spring")
public interface InmobiliriaApiMapper {

    @Mapping(target = "nombreProyectos", source = "request.proyectos")
    @Mapping(target = "logo", source = "file", qualifiedByName = "mapMultipartToFileCommand")
    RegistrarInmobiliariaCommand toCommand(InmobiliariaRequest request, MultipartFile file);




    @Named("mapMultipartToFileCommand")
    default UploadFileCommand mapMultipartToFileCommand(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            return new UploadFileCommand(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize(),
                    file.getInputStream() // Extraemos el stream aquí
            );
        } catch (IOException e) {
            // Convertimos error chequeado a Runtime para no ensuciar la firma
            throw new RuntimeException("Error al leer el archivo subido en el mapper", e);
        }
    }



}
