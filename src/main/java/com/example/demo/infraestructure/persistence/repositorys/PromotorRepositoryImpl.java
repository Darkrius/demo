package com.example.demo.infraestructure.persistence.repositorys;

import com.example.demo.application.exceptions.EntidadDuplicadaException;
import com.example.demo.application.exceptions.PersistenceException;
import com.example.demo.domain.model.Promotor;
import com.example.demo.domain.repository.PromotorRepository;
import com.example.demo.infraestructure.persistence.constants.StoredProcedureConstants;
import com.example.demo.infraestructure.persistence.entities.PromotorEntity;
import com.example.demo.infraestructure.persistence.mapper.MapperPromotor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
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
public class PromotorRepositoryImpl implements PromotorRepository {


    private final SimpleJdbcCall guardarPromotor;
    private final SimpleJdbcCall guardarProyectosPromotor;
    private final MapperPromotor mapperPromotor;

    public PromotorRepositoryImpl(@Qualifier("principalJdbcTemplate") JdbcTemplate jdbcTemplate, MapperPromotor mapperPromotor) {
        this.guardarPromotor = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_GUARDAR_PROMOTORES)
                .declareParameters(
                        new SqlParameter("Nombres", Types.VARCHAR),
                        new SqlParameter("Apellidos", Types.VARCHAR),
                        new SqlParameter("Doi", Types.VARCHAR),
                        new SqlParameter("Correo", Types.VARCHAR),
                        new SqlParameter("IdInmobiliaria", Types.BIGINT),
                        new SqlParameter("IdAdminCreador", Types.VARCHAR),
                        new SqlParameter("TipoPromotor", Types.VARCHAR)
                )
                .returningResultSet("idResultPromotor", (rs, rowNum) -> rs.getLong("idGenerado"));
        this.guardarProyectosPromotor = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_ASIGNAR_PROYECTOS_PROMOTOR)
                .declareParameters(
                        new SqlParameter("IdUsuario", Types.BIGINT),
                        new SqlParameter("IdProyecto", Types.BIGINT)
                );
        this.mapperPromotor = mapperPromotor;
    }


    @Override
    public Long guardarPromotor(Promotor promotor) {
        log.info("INFRA: Iniciando guardado de Asesor. ID: [{}]", promotor.getIdInmobiliaria());

        try{
            PromotorEntity promotorEntity = mapperPromotor.toEntity(promotor);
            Map<String, Object> listas = new HashMap<>();
            listas.put("Nombres", promotorEntity.getNombres());
            listas.put("Apellidos", promotorEntity.getApellidos());
            listas.put("Doi", promotorEntity.getDoi());
            listas.put("Correo", promotorEntity.getCorreo());
            listas.put("IdInmobiliaria", promotorEntity.getIdInmobiliaria());
            listas.put("IdAdminCreador", promotorEntity.getIdAdminEncargado());
            listas.put("TipoPromotor", promotorEntity.getTipoPromotor());

            Map<String, Object> result = guardarPromotor.execute(listas);

            @SuppressWarnings("unchecked")
            List<Long> ids = (List<Long>) result.get("idResultPromotor");

            if (ids != null && !ids.isEmpty()) {
                Long idGenerado = ids.getFirst();
                log.info("INFRA: Promotor guardada exitosamente. ID Generado: [{}]", idGenerado);
                return idGenerado;
            } else {
                throw new PersistenceException("El SP de guardar Promotor no devolvió el ID generado.");
            }
        } catch (DataIntegrityViolationException e) {
            // CORRECCIÓN VITAL: Traducimos el error de SQL a uno de Negocio
            // Esto pasa cuando el SP hace THROW 50001 (Duplicado)
            log.warn("INFRA WARN: Duplicado detectado en BD.");
            throw new EntidadDuplicadaException("El DOI o Correo ya existen en el sistema.");

        } catch (Exception e) {
            log.error("INFRA ERROR: Falló guardado técnico.", e);
            throw new PersistenceException("Error técnico al guardar promotor.", e);
        }

    }

    @Override
    public void guardarProyectosPromotor(Long idUsuario, Long idProyecto) {
        log.debug("INFRA: Asignando Proyecto [{}] a Promotor [{}]", idProyecto, idUsuario);

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("IdUsuario", idUsuario);
            params.put("IdProyecto", idProyecto);

            guardarProyectosPromotor.execute(params);
        } catch (DataIntegrityViolationException e) {
            log.error("INFRA ERROR: Violación de integridad en asignación.", e);
            throw new PersistenceException("No se pudo asignar el proyecto. Verifique que pertenezca a la misma empresa.", e);

        } catch (Exception e) {
            log.error("INFRA ERROR: Fallo técnico asignando proyecto.", e);
            throw new PersistenceException("Error al asignar proyecto.", e);
        }
    }
}
