package com.example.demo.infraestructure.persistence.mapper;

import com.example.demo.infraestructure.persistence.entities.ProyectosEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapperProyecto implements RowMapper<ProyectosEntity> {


    @Override
    public ProyectosEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProyectosEntity entity = new ProyectosEntity();
        entity.setIdProyecto(rs.getLong("idProyecto"));
        entity.setNombre(rs.getString("nombre"));
        entity.setEstado(rs.getBoolean("estado"));
        entity.setFechaCreacion(rs.getTimestamp("fechaCreacion").toLocalDateTime());
        entity.setFechaModificacion(rs.getTimestamp("fechaModificacion").toLocalDateTime());
        entity.setIdInmobiliaria(rs.getLong("idInmobiliaria"));
        return entity;
    }
}
