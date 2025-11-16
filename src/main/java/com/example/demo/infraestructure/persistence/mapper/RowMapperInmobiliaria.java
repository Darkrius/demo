package com.example.demo.infraestructure.persistence.mapper;

import com.example.demo.infraestructure.persistence.entities.InmobiliariasEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RowMapperInmobiliaria implements RowMapper<InmobiliariasEntity> {

    @Override
    public InmobiliariasEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

        Timestamp fc = rs.getTimestamp("fechaCreacion");
        Timestamp fm = rs.getTimestamp("fechaModificacion");

        return InmobiliariasEntity.builder()
                .idInmobiliaria(rs.getLong("idInmobiliaria"))
                .ruc(rs.getString("ruc"))
                .razonSocial(rs.getString("razonSocial"))
                .estado(rs.getBoolean("estado"))
                .fechaCreacion(fc != null ? fc.toLocalDateTime() : null)
                .fechaModificacion(fm != null ? fm.toLocalDateTime() : null)
                .idAdminCreador(rs.getString("idAdminCreador"))
                .build();

    }
}
