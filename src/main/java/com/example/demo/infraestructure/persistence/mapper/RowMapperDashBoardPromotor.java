package com.example.demo.infraestructure.persistence.mapper;

import com.example.demo.application.dto.query.PromotorDashBoard;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RowMapperDashBoardPromotor implements RowMapper<PromotorDashBoard> {




    @Override
    public PromotorDashBoard mapRow(ResultSet rs, int rowNum) throws SQLException {
        Timestamp fm = rs.getTimestamp("fechaModificacion");


        return new PromotorDashBoard(
                rs.getLong("idUsuario"),
                rs.getString("doi"),
                rs.getString("nombreApellido"),
                rs.getString("nombreInmobiliaria"),
                rs.getBoolean("estado"),
                fm != null ? fm.toLocalDateTime() : null
        );
    }
}
