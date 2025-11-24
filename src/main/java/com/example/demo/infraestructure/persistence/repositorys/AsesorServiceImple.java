package com.example.demo.infraestructure.persistence.repositorys;

import com.example.demo.domain.dto.AsesorDashBoard;
import com.example.demo.domain.model.Asesores;
import com.example.demo.domain.repository.AsesorRepository;
import com.example.demo.infraestructure.persistence.constants.StoredProcedureConstants;
import com.example.demo.infraestructure.persistence.entities.AsesoresEntity;
import com.example.demo.infraestructure.persistence.mapper.MapperAsesor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class AsesorServiceImple implements AsesorRepository {


    private final SimpleJdbcCall listarCall;
    private final SimpleJdbcCall guardarCall;
    private final SimpleJdbcCall buscarUbigeoCall;
    private final JdbcTemplate jdbcTemplate;

    private final MapperAsesor mapperAsesor;

    public AsesorServiceImple(@Qualifier("principalJdbcTemplate")JdbcTemplate jdbcTemplate, MapperAsesor mapperAsesor) {
        this.listarCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_LISTAR_ASESORES_GESTION)
                .declareParameters(
                        new SqlParameter("Nombre", Types.VARCHAR),
                        new SqlParameter("PageNumber", Types.INTEGER),
                        new SqlParameter("PageSize", Types.INTEGER)
                )
                .returningResultSet("data", (rs, rowNum) ->
                        new AsesorDashBoard(
                                rs.getString("idAsesor"),
                                rs.getString("nombreCompleto"),
                                rs.getString("nombreCiudad"),
                                rs.getString("tipoReferido"),
                                    rs.getLong("TotalRegistros")
                        )
                );
        this.guardarCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_GUARDAR_ASESOR)
                .declareParameters(
                        new SqlParameter("idAsesor", Types.VARCHAR),
                        new SqlParameter("nombres", Types.VARCHAR),
                        new SqlParameter("apellidos", Types.VARCHAR),
                        new SqlParameter("tipoReferido", Types.VARCHAR),
                        new SqlParameter("estado", Types.BIT),
                        new SqlParameter("fechaUltimaAsignacion", Types.TIMESTAMP), // O DATETIME
                        new SqlParameter("idUbigeo", Types.VARCHAR),
                        new SqlParameter("correoCorporativo", Types.VARCHAR)
                );
        this.buscarUbigeoCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_OBTENER_ID_UBIGEO)
                .declareParameters(
                        new SqlParameter("Distrito", Types.VARCHAR)
                )
                // Mapeamos para que devuelva solo el String
                .returningResultSet("result", (rs, rowNum) -> rs.getString("idUbigeo"));
        this.jdbcTemplate = jdbcTemplate;
        this.mapperAsesor = mapperAsesor;
    }

    @Override
    public void guardar(Asesores asesor) {
        log.info("INFRA: Iniciando guardado de Asesor. ID: [{}]", asesor.getIdAsesor());

        try {
            AsesoresEntity entity = mapperAsesor.toEntity(asesor);

            Map<String, Object> params = new HashMap<>();
            params.put("idAsesor", entity.getIdAsesor());
            params.put("nombres", entity.getNombres());
            params.put("apellidos", entity.getApellidos());
            params.put("tipoReferido", entity.getTipoReferido());
            params.put("estado", entity.isEstado());
            params.put("fechaUltimaAsignacion", entity.getFechaUltimaAsignacion());
            params.put("idUbigeo", entity.getIdUbigeo());
            params.put("correoCorporativo", entity.getCorreoCorporativo());

            guardarCall.execute(params);

            log.info("INFRA: Asesor guardado exitosamente en DB Gestión.");

        } catch (Exception e) {
            log.error("INFRA ERROR: Falló el guardado del asesor [{}]. Mensaje: {}", asesor.getIdAsesor(), e.getMessage());
            throw e;
        }

    }

    @Override
    public List<AsesorDashBoard> buscarPorNombreParcial(String nombre, int page, int size) {
        log.info("INFRA: Consultando Dashboard Interno. Page: [{}], Nombre: [{}]", page, nombre);

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("Nombre", nombre);
            params.put("PageNumber", page);
            params.put("PageSize", size);

            Map<String, Object> out = listarCall.execute(params);

            List<AsesorDashBoard> result = (List<AsesorDashBoard>) out.get("data");

            log.info("INFRA: Consulta Dashboard finalizada. Registros recuperados: [{}]", result.size());
            return result;

        } catch (Exception e) {
            log.error("INFRA ERROR: Falló SP_LISTAR_ASESORES_GESTION. Causa: {}", e.getMessage());
            throw e;
        }
    }



    @Override
    public Optional<String> buscarIdUbigeoPorCiudad(String ciudad) {
        log.debug("INFRA: Buscando ID Ubigeo para ciudad: [{}]", ciudad);

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("Distrito", ciudad);

            Map<String, Object> out = buscarUbigeoCall.execute(params);

            List<String> resultados = (List<String>) out.get("result");

            if (resultados != null && !resultados.isEmpty()) {
                return Optional.of(resultados.get(0));
            } else {
                log.warn("INFRA WARN: Ciudad [{}] no encontrada.", ciudad);
                return Optional.empty();
            }

        } catch (Exception e) {
            log.error("INFRA ERROR: Falló SP_OBTENER_ID_UBIGEO.", e);
            throw e;
        }
    }
}
