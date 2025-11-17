package com.example.demo.infraestructure.persistence.mapper;

import com.example.demo.domain.repository.DashBoardInmobiliaria;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RowMapperInmobiliariaDash implements RowMapper<DashBoardInmobiliaria> {

    @Override
    public DashBoardInmobiliaria mapRow(ResultSet rs, int rowNum) throws SQLException {
        Timestamp fm = rs.getTimestamp("fechaModificacion");
        Long id = rs.getObject("idInmobiliaria") == null ? null : rs.getLong("idInmobiliaria");
        String ruc = rs.getString("ruc");
        String razonSocial = rs.getString("razonSocial");
        boolean estado = rs.getBoolean("estado");
        java.time.LocalDateTime fechaMod = fm != null ? fm.toLocalDateTime() : null;
        int nProyectos = rs.getInt("nProyectos");

        return new DashBoardInmobiliaria(id, ruc, razonSocial, estado, fechaMod, nProyectos);
    }
}
