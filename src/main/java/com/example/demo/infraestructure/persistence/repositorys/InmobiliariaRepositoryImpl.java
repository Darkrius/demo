package com.example.demo.infraestructure.persistence.repositorys;

import com.example.demo.application.exceptions.EntidadDuplicadaException;
import com.example.demo.application.exceptions.PersistenceException;
import com.example.demo.application.exceptions.ReglasNegocioException;
import com.example.demo.domain.dto.EditarInmobiliaria;
import com.example.demo.domain.model.Inmobiliarias;
import com.example.demo.domain.model.Proyectos;
import com.example.demo.domain.repository.InmobilariaRepository;
import com.example.demo.infraestructure.persistence.constants.StoredProcedureConstants;
import com.example.demo.infraestructure.persistence.entities.InmobiliariasEntity;
import com.example.demo.infraestructure.persistence.entities.ProyectosEntity;
import com.example.demo.infraestructure.persistence.mapper.MapperInmobiliaria;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class InmobiliariaRepositoryImpl implements InmobilariaRepository {


    private final SimpleJdbcCall guardarInmobiliaria;
    private final SimpleJdbcCall guardarProyectos;
    private final SimpleJdbcCall buscarRazonSocialPorId;
    private final SimpleJdbcCall editarInmobiliariassp;
    private final MapperInmobiliaria mapperInmobiliaria;
    private final ObjectMapper objectMapper;


    public InmobiliariaRepositoryImpl(@Qualifier("principalJdbcTemplate") JdbcTemplate jdbcTemplate, MapperInmobiliaria mapperInmobiliaria, ObjectMapper objectMapper) {
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
        this.buscarRazonSocialPorId = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_BUSCAR_RAZONSOCIAL)
                .declareParameters(
                        new SqlParameter("IdInmobiliaria", Types.BIGINT)
                )
                .returningResultSet("razonSocial", (rs, rowNum) -> rs.getString("razonSocial"));
        this.mapperInmobiliaria = mapperInmobiliaria;
        this.editarInmobiliariassp = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_EDITAR_INMOBILIARIA)
                .declareParameters(
                        new SqlParameter("IdInmobiliaria", Types.BIGINT),
                        new SqlParameter("Estado", Types.BIT),
                        new SqlParameter("JsonNombresNuevos", Types.VARCHAR),
                        new SqlParameter("JsonIdsEliminar", Types.VARCHAR),
                        //parametros de salida
                        new SqlOutParameter("cCodigoRespuesta", Types.VARCHAR),
                        new SqlOutParameter("cMensajeRespuesta", Types.VARCHAR)
                );
        this.objectMapper = objectMapper;
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

    @Override
    public Optional<String> buscarRazonSocialPorId(Long idInmobiliaria) {
        log.debug("INFRA: Buscando Razón Social para ID: [{}]", idInmobiliaria);

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("IdInmobiliaria", idInmobiliaria);

            Map<String, Object> out = buscarRazonSocialPorId.execute(params);

            List<String> resultados = (List<String>) out.get("razonSocial");

            if (resultados != null && !resultados.isEmpty()) {
                return Optional.ofNullable(resultados.get(0));
            }

            return Optional.empty();

        } catch (Exception e) {
            log.error("INFRA ERROR: Falló SP_OBTENER_RAZON_SOCIAL.", e);
            return Optional.empty();
        }
    }

    @Override
    public void guardarEdicion(EditarInmobiliaria editarInmobiliaria) {

        log.info("INFRA: Iniciando edición de Inmobiliaria ID: [{}]", editarInmobiliaria.idInmobiliaria());

        try {
            String jsonNombres = objectMapper.writeValueAsString(editarInmobiliaria.nombreProyectos());
            String jsonIds = objectMapper.writeValueAsString(editarInmobiliaria.idProyectos());

            // 2. Preparar parámetros del SP
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("IdInmobiliaria", editarInmobiliaria.idInmobiliaria());
            parametros.put("Estado", editarInmobiliaria.estado());
            parametros.put("JsonNombresNuevos", jsonNombres);
            parametros.put("JsonIdsEliminar", jsonIds);

            Map<String, Object> out = editarInmobiliariassp.execute(parametros);

            String codigo = (String) out.get("cCodigoRespuesta");
            String mensaje = (String) out.get("cMensajeRespuesta");

            if (!"00".equals(codigo)) {
                log.warn("INFRA WARN: Regla de Negocio detectada en BD. Código: [{}], Mensaje: [{}]", codigo, mensaje);

                // 1. Detectar duplicados
                if (mensaje != null && mensaje.toLowerCase().contains("ya existe")) {
                    throw new EntidadDuplicadaException(mensaje);
                }

                // 2. Detectar error de negocio genérico (ej. promotores activos)
                throw new ReglasNegocioException(mensaje);
            }

            log.info("INFRA: Edición exitosa en base de datos.");

        } catch (JsonProcessingException e) {
            log.error("INFRA ERROR: Fallo al serializar JSON.", e);
            throw new PersistenceException("Error interno al procesar datos.", e);

        } catch (Exception e) {
            // Dejar pasar las excepciones que nosotros lanzamos arriba
            if (e instanceof ReglasNegocioException || e instanceof EntidadDuplicadaException) {
                throw e;
            }

            log.error("INFRA ERROR: Fallo crítico en BD para ID [{}].", editarInmobiliaria.idInmobiliaria(), e);
            throw new PersistenceException("Error inesperado en base de datos: " + e.getMessage(), e);
        }


    }
}
