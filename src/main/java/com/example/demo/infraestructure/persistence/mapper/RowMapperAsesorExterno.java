package com.example.demo.infraestructure.persistence.mapper;

import com.example.demo.infraestructure.persistence.entities.AsesorExternoEntity;
import com.example.demo.infraestructure.persistence.entities.AsesoresEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RowMapperAsesorExterno implements RowMapper<AsesorExternoEntity> {


    @Override
    public AsesorExternoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AsesorExternoEntity entity = new AsesorExternoEntity();
        entity.setIdAsesorAD(rs.getString("idAsesorAD"));
        entity.setNombres(rs.getString("nombres"));
        entity.setApellidos(rs.getString("apellidos"));
        entity.setCorreoCorporativo(rs.getString("correoCorporativo"));
        entity.setDoi(rs.getString("doi"));
        entity.setCiudad(rs.getString("ciudad"));
        return entity;
    }
}
