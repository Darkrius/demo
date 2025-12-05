package com.example.demo.infraestructure.persistence.mapper;

import com.example.demo.infraestructure.persistence.entities.PromotorEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapperPromotor implements RowMapper<PromotorEntity> {


    @Override
    public PromotorEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PromotorEntity.builder()
                .idUsuario(rs.getLong("IdUsuario"))
                .nombres(rs.getString("Nombres"))
                .apellidos(rs.getString("Apellidos"))
                .doi(rs.getString("Doi"))
                .correo(rs.getString("Correo"))
                .idAdminEncargado(rs.getString("IdAdminEncargado"))
                .idInmobiliaria(rs.getLong("IdInmobiliaria"))
                .tipoPromotor(rs.getString("TipoPromotor"))
                .estado(rs.getBoolean("Estado"))
                .fechaCreacion(rs.getTimestamp("fechaCreacion").toLocalDateTime())
                .fechaModificacion(rs.getTimestamp("fechaModificacion").toLocalDateTime())
                .fechaEliminacion(rs.getTimestamp("fechaEliminacion") != null ? rs.getTimestamp("fechaEliminacion").toLocalDateTime() : null)
                .build();
    }
}
