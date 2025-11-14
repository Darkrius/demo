package com.example.demo.infraestructure.persistence.mapper;

import com.example.demo.infraestructure.persistence.entities.AsesoresEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapperAsesor implements RowMapper<AsesoresEntity> {

    @Override
    public AsesoresEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AsesoresEntity entity = new AsesoresEntity();
        entity.setIdAsesor(rs.getString("idAsesor"));
        entity.setNombres(rs.getString("nombres"));
        entity.setApellidos(rs.getString("apellidos"));
        entity.setCorreoCorporativo(rs.getString("correoCorporativo"));
        entity.setTipoReferido(rs.getString("tipoReferido"));
        entity.setEstado(rs.getBoolean("estado"));
        entity.setFechaUltimaAsignacion(
                rs.getTimestamp("fechaUltimaAsignacion") != null
                        ? rs.getTimestamp("fechaUltimaAsignacion").toLocalDateTime()
                        : null
        );        entity.setIdUbigeo(rs.getLong("idUbigeo"));
        return entity;
    }
}
