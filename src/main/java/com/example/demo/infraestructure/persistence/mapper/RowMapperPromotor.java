package com.example.demo.infraestructure.persistence.mapper;

import com.example.demo.infraestructure.persistence.entities.PromotorEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RowMapperPromotor implements RowMapper<PromotorEntity> {


    @Override
    public PromotorEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        Timestamp fc = rs.getTimestamp("fechaCreacion");
        Timestamp fm = rs.getTimestamp("fechaModificacion");

        return PromotorEntity.builder()
                .idUsuario(rs.getLong("idUsuario"))
                .nombres(rs.getString("nombres"))
                .apellidos(rs.getString("apellidos"))
                .doi(rs.getString("doi"))
                .correo(rs.getString("correo"))
                .idInmobiliaria(rs.getLong("idInmobiliaria"))
                .idAdminEncargado(rs.getString("idAdminEncargado"))
                .estado(rs.getBoolean("estado"))
                .fechaCreacion(fc != null ? fc.toLocalDateTime() : null)
                .fechaModificacion(fm != null ? fm.toLocalDateTime() : null)
                .build();
    }
}
