package com.example.demo.application.dto.queries;

import java.util.List;

public record PromotorDetalleDto(
        Long idUsuario,
       String nombres,
      String apellidos,
      String  doi,
      String email,
      boolean estado,
        Long idInmobiliaria,
        String  ruc,
        String razonSocial,
        List<ProyectoAsignado> proyectosAsignados,
        List<ProspectoCaptado> prospectosCaptados
) {


    public record ProyectoAsignado(
            Long idProyecto,
            String nombreProyecto
    ) {}

    public record ProspectoCaptado(
            Long idProspecto,
            String nombreTitular,
            Long idProyectoInteres,
            String nombreProyectoInteres
    ) {}
}

