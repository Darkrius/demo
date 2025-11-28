package com.example.demo.infraestructure.persistence.repositorys;

import com.example.demo.application.exceptions.PersistenceException;
import com.example.demo.domain.model.Inmobiliarias;
import com.example.demo.domain.model.Proyectos;
import com.example.demo.domain.repository.InmobilariaRepository;
import com.example.demo.infraestructure.persistence.constants.StoredProcedureConstants;
import com.example.demo.infraestructure.persistence.entities.InmobiliariasEntity;
import com.example.demo.infraestructure.persistence.entities.ProyectosEntity;
import com.example.demo.infraestructure.persistence.mapper.MapperInmobiliaria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class InmobiliariaRepositoryImpl implements InmobilariaRepository {


    private final SimpleJdbcCall guardarInmobiliaria;
    private final SimpleJdbcCall guardarProyectos;
    private final MapperInmobiliaria mapperInmobiliaria;


    public InmobiliariaRepositoryImpl(@Qualifier("principalJdbcTemplate") JdbcTemplate jdbcTemplate, MapperInmobiliaria mapperInmobiliaria) {
        this.guardarInmobiliaria = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_GUARDAR_INMOBILIARIA)
                .declareParameters(
                new SqlParameter("Ruc", Types.VARCHAR),
                new SqlParameter("RazonSocial", Types.VARCHAR),
                new SqlParameter("IdAdminCreador", Types.VARCHAR),
                new SqlParameter("LogoUrl", Types.VARCHAR)
                )
        .returningResultSet("idResult", (rs, rowNum) -> rs.getLong("idGenerado"));
        this.guardarProyectos = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_GUARDAR_PROYECTO)
                .declareParameters(
                        new SqlParameter("Nombre" , Types.VARCHAR),
                        new SqlParameter("IdInmobiliaria", Types.BIGINT)
                );
        this.mapperInmobiliaria = mapperInmobiliaria;
    }


    @Override
    public Long guardarInmobiliaria(Inmobiliarias inmobiliaria) {
        log.info("INFRA: Iniciando guardado de Asesor. ID: [{}]", inmobiliaria.getIdInmobiliaria());

        try {

            InmobiliariasEntity entity = mapperInmobiliaria.toEntity(inmobiliaria);

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("Ruc", entity.getRuc());
            parametros.put("RazonSocial", entity.getRazonSocial());
            parametros.put("IdAdminCreador", entity.getIdAdminCreador());
            parametros.put("LogoUrl", entity.getLogoUrl());

            Map<String, Object> result = guardarInmobiliaria.execute(parametros);

            @SuppressWarnings("unchecked")
            List<Long> ids = (List<Long>) result.get("idResult");

            if (ids != null && !ids.isEmpty()) {
                Long idGenerado = ids.getFirst();
                log.info("INFRA: Inmobiliaria guardada exitosamente. ID Generado: [{}]", idGenerado);
                return idGenerado;
            } else {
                throw new PersistenceException("El SP de guardar inmobiliaria no devolvió el ID generado.");
            }

        } catch (Exception e) {
            log.error("INFRA ERROR: Falló el guardado de inmobiliaria [{}]. Mensaje: {}", inmobiliaria.getRuc(), e.getMessage());
            throw new PersistenceException("Error al guardar inmobiliaria: " + e.getMessage(), e);
        }
    }

    @Override
    public void guardarProyectos(Proyectos proyectos, Long idInmobiliaria) {
        log.debug("INFRA: Guardando proyecto [{}] para Inmobiliaria ID [{}]", proyectos.getNombre(), idInmobiliaria);

        try {
            ProyectosEntity entity = mapperInmobiliaria.toEntity(proyectos);

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("Nombre", entity.getNombre());
            parametros.put("IdInmobiliaria", idInmobiliaria); // Usamos el ID que nos pasaron

            guardarProyectos.execute(parametros);

        } catch (Exception e) {
            log.error("INFRA ERROR: Falló el guardado del proyecto [{}].", proyectos.getNombre(), e);
            throw e;
        }
    }
}
