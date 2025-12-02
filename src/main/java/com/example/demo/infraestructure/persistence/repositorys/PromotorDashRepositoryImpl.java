package com.example.demo.infraestructure.persistence.repositorys;

import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.PromotorDashBoardDto;
import com.example.demo.application.dto.queries.PromotorDetalleDto;
import com.example.demo.application.exceptions.PersistenceException;
import com.example.demo.application.interfaces.external.PromotorPortService;
import com.example.demo.domain.dto.PromotorDashBoard;
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
import java.util.Optional;


@Slf4j
@Repository
public class PromotorDashRepositoryImpl implements PromotorPortService {

    private final SimpleJdbcCall guardarPromotorJdbcCall;
    private final SimpleJdbcCall listarPromotorID;


    public PromotorDashRepositoryImpl(@Qualifier("principalJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.guardarPromotorJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_LISTAR_PROMOTORES)
                .declareParameters(
                        new SqlParameter("idAdminCreador", Types.VARCHAR),
                        new SqlParameter("PageNumber", Types.INTEGER),
                        new SqlParameter("PageSize", Types.INTEGER)
                )
                .returningResultSet("data", (rs, rowNum) ->
                        new PromotorDashBoard(
                                rs.getLong("idUsuario"),
                                rs.getString("nombreCompleto"),
                                rs.getString("nombreInmobiliaria"),
                                rs.getBoolean("estado"),
                                rs.getTimestamp("fechaModificacion").toLocalDateTime(),
                                rs.getLong("TotalRegistros")
                        )
                );
        this.listarPromotorID = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_BUSCAR_PROMOTOR_ID)
                .declareParameters(
                        new SqlParameter("idUsuario", Types.BIGINT)
                )
                .returningResultSet("promotor", (rs, rowNum) -> new PromotorHeaderTemp(
                        rs.getLong("idUsuario"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("doi"),
                        rs.getString("correo"),
                        rs.getBoolean("estado"),
                        rs.getLong("idInmobiliaria"),
                        rs.getString("ruc"),
                        rs.getString("nombreInmobiliaria")
                )
                )
                .returningResultSet("proyectos", (rs, rowNum) -> new PromotorDetalleDto.ProyectoAsignado(
                        rs.getLong("idProyecto"),
                        rs.getString("nombreProyecto")
                        )
                )
                .returningResultSet("prospectos", (rs, rowNum) -> new PromotorDetalleDto.ProspectoCaptado(
                        rs.getLong("idProspecto"),
                        rs.getString("nombreTitular"),
                        rs.getLong("idProyecto"),
                        rs.getString("nombreProyectoInteres")
                )
                );

    }


    @Override
    public PaginationResponseDTO<PromotorDashBoardDto> promotorListar(String idAdminCreador, int page, int size) {
        log.info("INFRA: Consultando Dashboard Interno. Page: [{}], Nombre: [{}]", page, idAdminCreador);

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idAdminCreador", idAdminCreador);
            params.put("PageNumber", page);
            params.put("PageSize", size);

            Map<String, Object> out = guardarPromotorJdbcCall.execute(params);

            @SuppressWarnings("unchecked")
            List<PromotorDashBoard> rawList = (List<PromotorDashBoard>) out.get("data");

            long totalElements = 0;
            List<PromotorDashBoardDto> content = List.of();

            if (rawList != null && !rawList.isEmpty()) {
                totalElements = rawList.get(0).totalRegistros();
                content = rawList.stream()
                        .map(domain -> new PromotorDashBoardDto(
                                domain.idUsuario(),
                                domain.nombreCompleto(),
                                domain.nombreInmobiliaria(),
                                domain.estado(),
                                domain.fechaModificacion()
                        )).toList();
            }

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
            log.error("INFRA ERROR: Falló SP_lISTAR_PROMOTOR.", e);
            throw new PersistenceException("Error de base de datos al listar promotor.", e);
        }

        }

    @Override
    public Optional<PromotorDetalleDto> listarPromotorPorId(Long idUsuario) {
        log.info("INFRA: Consultando Get del Promotor Interno. idUsuario: [{}]" , idUsuario);


        try{
            Map<String, Object> params = new HashMap<>();
            params.put("idUsuario", idUsuario);
            Map<String, Object> out = listarPromotorID.execute(params);

            List<PromotorHeaderTemp> promotorList = (List<PromotorHeaderTemp>) out.get("promotor");
            if (promotorList.isEmpty()) return Optional.empty();
            PromotorHeaderTemp promotor = promotorList.get(0);

            List<PromotorDetalleDto.ProyectoAsignado> proyectos = (List<PromotorDetalleDto.ProyectoAsignado>) out.get("proyectos");
            List<PromotorDetalleDto.ProspectoCaptado> prospectos = (List<PromotorDetalleDto.ProspectoCaptado>) out.get("prospectos");


            PromotorDetalleDto detalleDto = new PromotorDetalleDto(
                    promotor.id(),
                    promotor.nombres(),
                    promotor.apellidos(),
                    promotor.doi(),
                    promotor.correo(),
                    promotor.estado(),
                    promotor.idInmo(),
                    promotor.rucInmo(),
                    promotor.nombreInmo(),
                    proyectos,
                    prospectos
            );

            return Optional.of(detalleDto);
        }catch (Exception e) {
            log.error("INFRA ERROR: Falló SP_OBTENER_PROMOTOR_POR_ID.", e);
            throw new PersistenceException("Error al obtener detalle del promotor.", e);
        }
    }


    private record PromotorHeaderTemp(
            Long id, String nombres, String apellidos, String doi, String correo,
            boolean estado, Long idInmo, String rucInmo, String nombreInmo
    ) {}
}
