package com.example.demo.infraestructure.persistence.repositorys;

import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.AsesorLegacyDto;
import com.example.demo.application.interfaces.external.AsesorLegacyService;
import com.example.demo.infraestructure.persistence.constants.StoredProcedureConstants;
import com.example.demo.infraestructure.persistence.entities.AsesoresEntity;
import com.example.demo.infraestructure.persistence.mapper.MapperAsesor;
import com.example.demo.infraestructure.persistence.mapper.RowMapperAsesorExterno;
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

@Repository
@Slf4j
public class AsesorExternoImple implements AsesorLegacyService {

    private final SimpleJdbcCall listarcall;



    public AsesorExternoImple(@Qualifier("legacyJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.listarcall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_LISTAR_CANDIDATOS)
                .declareParameters(
                        new SqlParameter("Nombre", Types.VARCHAR),
                        new SqlParameter("Ciudad", Types.VARCHAR),
                        new SqlParameter("PageNumber", Types.INTEGER),
                        new SqlParameter("PageSize", Types.INTEGER)
                )
                .returningResultSet("resultados", (rs, rowNum) -> {

                    AsesoresEntity entity = new RowMapperAsesorExterno().mapRow(rs, rowNum);
                    long total = rs.getLong("TotalRegistros");
                    String ciudadReal = rs.getString("ciudad"); // "Ica", "Pisco"...
                    return new SpResult(entity, total, ciudadReal);
                });
    }

    @Override
    public PaginationResponseDTO<AsesorLegacyDto> listar(int page, int size, String nombre, String ciudad) {
        log.info("INFRA LEGACY: Iniciando consulta a base externa. Page: [{}], Size: [{}], Filtros: Nombre=[{}], Ciudad=[{}]",
                page, size, nombre, ciudad);

        Map<String, Object> params = new HashMap<>();
        params.put("Nombre", nombre);
        params.put("Ciudad", ciudad);
        params.put("PageNumber", page);
        params.put("PageSize", size);

        // 2. Ejecutar SP
        try {

            // --- EJECUCIÓN ---
            Map<String, Object> out = listarcall.execute(params);
            // -----------------
            @SuppressWarnings("unchecked")
            List<SpResult> resultados = (List<SpResult>) out.get("resultados");
            long totalElements = 0;
            List<AsesorLegacyDto> content = List.of();

            if (resultados != null && !resultados.isEmpty()) {
                totalElements = resultados.get(0).total();
                content = resultados.stream()
                        .map(res -> new AsesorLegacyDto(
                                res.entity().getIdAsesor(),
                                res.entity().getNombres() + " " + res.entity().getApellidos(),
                                res.ciudadRaw()
                        ))
                        .toList();
            }
            // 5. LOG DE ÉXITO
            log.info("INFRA LEGACY: Registros recuperados: [{}]", content.size());
            int totalPages = (size > 0) ? (int) Math.ceil((double) totalElements / size) : 0;
            boolean isLast = page >= totalPages;
            return new PaginationResponseDTO<>(
                    content, page, size, totalElements, totalPages, isLast
            );

        } catch (Exception e) {
            log.error("INFRA LEGACY ERROR: Falló la ejecución de SP_LISTAR_CANDIDATOS. Mensaje: {}", e.getMessage(), e);
            throw e; // Re-lanzamos la excepción para que el ControllerAdvice responda al cliente
        }
    }

    private record SpResult(AsesoresEntity entity, long total, String ciudadRaw) {}
}
