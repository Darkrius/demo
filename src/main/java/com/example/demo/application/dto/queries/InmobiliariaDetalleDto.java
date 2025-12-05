package com.example.demo.application.dto.queries;

import java.util.List;

public record InmobiliariaDetalleDto(
        Long idInmobiliaria,
        String ruc,
        String razonSocial,
        boolean estado,
        List<ProyectosDto> proyectos,
        List<PromotoresProyectoDto> promotoresProyectos
) {

    public record ProyectosDto(Long idProyecto, String nombreProyecto){}

    public record PromotoresProyectoDto(Long idProyecto, String nombreProyecto ,Long idPromotor, String nombrePromotor){}
}
