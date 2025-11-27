package com.example.demo.infraestructure.persistence.repositorys;


import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.InmobiliariaDashBoardDto;
import com.example.demo.application.exceptions.PersistenceException;
import com.example.demo.application.interfaces.external.InmobiliariaPortService;
import com.example.demo.domain.dto.InmobiliariaDashBoard;
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
public class InmobiliariaDashServiceImpl implements InmobiliariaPortService {

    private final SimpleJdbcCall listarCall;

    public InmobiliariaDashServiceImpl(@Qualifier("principalJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.listarCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_LISTAR_INMOBILIARIAS)
                .declareParameters(
                        new SqlParameter("idAdminCreador", Types.VARCHAR),
                        new SqlParameter("PageNumber", Types.INTEGER),
                        new SqlParameter("PageSize", Types.INTEGER)
                )
                .returningResultSet("data", (rs, rowNum) ->
                        new InmobiliariaDashBoard(
                                rs.getLong("idInmobiliaria"),
                                rs.getString("ruc"),
                                rs.getString("razonSocial"),
                                rs.getInt("nProyectos"),
                                rs.getBoolean("estado"),
                                rs.getTimestamp("fechaModificacion").toLocalDateTime(),
                                rs.getLong("TotalRegistros")
                        )
                );
    }

    @Override
    public PaginationResponseDTO<InmobiliariaDashBoardDto> inmobiliariaListar(int page, int size, String idAdminCreador) {
        log.info("INFRA: Consultando Dashboard Interno. Page: [{}], Nombre: [{}]", page, idAdminCreador);

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idAdminCreador", idAdminCreador);
            params.put("PageNumber", page);
            params.put("PageSize", size);

            Map<String, Object> out = listarCall.execute(params);

            //usamos el dto que esta en application
            @SuppressWarnings("unchecked")
            List<InmobiliariaDashBoard> rawList = (List<InmobiliariaDashBoard>) out.get("data");

            // 2. Procesar Resultados
            long totalElements = 0;
            List<InmobiliariaDashBoardDto> content = List.of();

            if (rawList != null && !rawList.isEmpty()) {
                // A. Extraemos el Total Global (viene en la primera fila)
                totalElements = rawList.getFirst().totalRegistros(); // Asumiendo que el record tiene este getter

                // B. Mapeamos Dominio -> Application DTO (Limpieza)
                content = rawList.stream()
                        .map(domain -> new InmobiliariaDashBoardDto(
                                domain.idInmobiliaria(),
                                domain.ruc(),
                                domain.razonSocial(),
                                domain.nProyectos(),
                                domain.estado(),
                                domain.fechaModificacion()
                                // Agrega logoUrl aquí si decidiste ponerlo en el DTO final
                        ))
                        .toList();
            }

            // 3. Calcular Paginación
            int totalPages = (size > 0) ? (int) Math.ceil((double) totalElements / size) : 0;
            boolean isLast = page >= totalPages;

            log.info("INFRA: Listado exitoso. Total registros encontrados: [{}]", totalElements);

            return new PaginationResponseDTO<>(
                    content,
                    page,
                    size,
                    totalElements,
                    totalPages,
                    isLast
            );

        } catch (Exception e) {
            log.error("INFRA ERROR: Falló SP_LISTAR_INMOBILIARIA.", e);
            throw new PersistenceException("Error de base de datos al listar inmobiliarias.", e);
        }

    }
}
