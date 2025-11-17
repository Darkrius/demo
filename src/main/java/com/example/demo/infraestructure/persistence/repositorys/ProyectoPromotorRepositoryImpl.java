package com.example.demo.infraestructure.persistence.repositorys;

import com.example.demo.domain.repository.ProyectoPromotorRepository;
import com.example.demo.infraestructure.persistence.constants.StoredProcedureConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ProyectoPromotorRepositoryImpl implements ProyectoPromotorRepository {

    private final SimpleJdbcCall saveCall;
    private final ObjectMapper objectMapper;

    public ProyectoPromotorRepositoryImpl(@Qualifier("jdbcTemplate")JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.saveCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_ASIGNAR_PROYECTOS_PROMOTORES)
                .declareParameters(
                        new SqlParameter("idPromotor", Types.BIGINT),
                        new SqlParameter("proyectosJSON", Types.NVARCHAR)
                );
    }

    @Override
    public void asginarPromotor(Long idPromotor, List<Long> idProyectos) {
        if (idProyectos == null || idProyectos.isEmpty()) {
            return;
        }
        String proyectosJSON;
        try {
            List<Map<String, Long>> jsonList = idProyectos.stream()
                    .map(id -> Map.of("id", id))
                    .collect(Collectors.toList());
            proyectosJSON = objectMapper.writeValueAsString(jsonList);
        } catch (Exception e) {
            throw new RuntimeException("Error fatal convirtiendo la lista de proyectos a JSON", e);
        }
        Map<String, Object> params = Map.of(
                "idPromotor", idPromotor,
                "proyectosJSON", proyectosJSON
        );
        saveCall.execute(params);
    }


    }

