package com.example.demo.infraestructure.persistence.mapper;

import com.example.demo.infraestructure.persistence.entities.InmobiliariasEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapperClientInmobiliaria implements RowMapper<InmobiliariasEntity> {

    @Override
    public InmobiliariasEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return InmobiliariasEntity.builder()
                .idInmobiliaria(rs.getLong("idInmobiliaria"))
                .razonSocial(rs.getString("razonSocial"))
                .build();
    }
}
