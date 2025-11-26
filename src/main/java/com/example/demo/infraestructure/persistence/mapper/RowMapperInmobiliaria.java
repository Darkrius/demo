package com.example.demo.infraestructure.persistence.mapper;

import com.example.demo.infraestructure.persistence.entities.InmobiliariasEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapperInmobiliaria implements RowMapper<InmobiliariasEntity> {


    @Override
    public InmobiliariasEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return InmobiliariasEntity.builder()
                .idInmobiliaria(rs.getLong("idInmobiliaria"))
                .razonSocial(rs.getString("razonSocial"))
                .ruc(rs.getString("ruc"))
                .estado(rs.getBoolean("estado"))
                .fechaCreacion(rs.getTimestamp("fechaCreacion").toLocalDateTime())
                .fechaModificacion(rs.getTimestamp("fechaModificacion").toLocalDateTime())
                .idAdminCreador(rs.getString("idAdminCreador"))
                .logoUrl(rs.getString("logoUrl"))
                .build();
    }
}
