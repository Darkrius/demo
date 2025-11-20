package com.example.demo.infraestructure.persistence.mapper;

import com.example.demo.infraestructure.persistence.entities.ProyectosEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapperProyectoLookup implements RowMapper<ProyectosEntity> {
    @Override
    public ProyectosEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ProyectosEntity.builder()
                .idProyecto(rs.getLong("idProyecto"))
                .nombre(rs.getString("nombre"))
                .build();

    }
}
