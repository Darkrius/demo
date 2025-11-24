package com.example.demo.infraestructure.persistence.mapper;

import com.example.demo.infraestructure.persistence.entities.AsesoresEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapperAsesorExterno implements RowMapper<AsesoresEntity> {
    @Override
    public AsesoresEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

        //para los asesores externos solo ponemos lo que viene y solo vienen 3 cosas
        //id , nombreCompleto y correo corporativo
        return AsesoresEntity.builder()
                .idAsesor(rs.getString("idAsesorAD"))
                .nombres(rs.getString("nombres"))
                .apellidos(rs.getString("apellidos"))
                .tipoReferido(rs.getString("correoCorporativo"))
                .tipoReferido(null)
                .estado(false)
                .fechaUltimaAsignacion(null)
                .idUbigeo(null)
                .build();
    }
}
