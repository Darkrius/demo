package com.example.demo.infraestructure.persistence.repositorys;

import com.example.demo.application.dto.SelectorDto;
import com.example.demo.application.exceptions.PersistenceException;
import com.example.demo.application.interfaces.external.ISelectorService;
import com.example.demo.infraestructure.persistence.constants.StoredProcedureConstants;
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
public class SelectorRepositoryImpl implements ISelectorService {

    private final SimpleJdbcCall listarCallInmobiliaria;
    private final SimpleJdbcCall listarCallProyecto;

    public SelectorRepositoryImpl(@Qualifier("principalJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.listarCallInmobiliaria = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_SELECTOR_INMOBILIARIAS)
                .declareParameters(
                        new SqlParameter("idAdminCreador", Types.VARCHAR)
                )
                .returningResultSet("inmobiliarias", (rs, rowNum) -> new SelectorDto(
                        rs.getLong("idInmobiliaria"),
                        rs.getString("razonSocial")));
        this.listarCallProyecto =  new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_SELECTOR_PROYECTOS)
                .declareParameters(
                        new SqlParameter("idInmobiliaria", Types.BIGINT)
                ).returningResultSet("proyectos", (rs, rowNum) -> new SelectorDto(
                        rs.getLong("idProyecto"),
                        rs.getString("nombre")
                ));
    }


    @Override
    public List<SelectorDto> listarInmobiliarias(String idAdminCreador) {
        log.info("INFRA: Consultando Listas de inmobiliarias segun Admin: [{}]" , idAdminCreador);

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idAdminCreador", idAdminCreador);

            Map<String, Object> out = listarCallInmobiliaria.execute(params);

            @SuppressWarnings("unchecked")
            List<SelectorDto> lista = (List<SelectorDto>) out.get("inmobiliarias");
            return (lista != null) ? lista : List.of();
        } catch (Exception e) {
            log.error("INFRA ERROR: Falló selector inmobiliarias.", e);
            throw new PersistenceException("Error al cargar selector de inmobiliarias", e);
        }

    }

    @Override
    public List<SelectorDto> listarProyectos(Long idInmobiliaria) {
        log.debug("INFRA: Ejecutando SP selector proyectos. Inmobiliaria ID: [{}]", idInmobiliaria);
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idInmobiliaria", idInmobiliaria);

            Map<String, Object> out = listarCallProyecto.execute(params);

            @SuppressWarnings("unchecked")
            List<SelectorDto> lista = (List<SelectorDto>) out.get("proyectos");
            return (lista != null) ? lista : List.of();

        } catch (Exception e) {
            log.error("INFRA ERROR: Falló selector proyectos.", e);
            throw new PersistenceException("Error al cargar selector de proyectos", e);
        }
    }
}
