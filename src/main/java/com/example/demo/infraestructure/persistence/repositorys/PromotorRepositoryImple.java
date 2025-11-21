package com.example.demo.infraestructure.persistence.repositorys;

import com.example.demo.domain.entities.Promotor;
import com.example.demo.application.dto.query.PromotorDashBoard;
import com.example.demo.domain.repository.PromotorRepository;
import com.example.demo.infraestructure.persistence.constants.StoredProcedureConstants;
import com.example.demo.infraestructure.persistence.entities.PromotorEntity;
import com.example.demo.infraestructure.persistence.mapper.PromotorMapper;
import com.example.demo.infraestructure.persistence.mapper.RowMapperDashBoardPromotor;
import com.example.demo.infraestructure.persistence.mapper.RowMapperPromotor;
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

@Repository
public class PromotorRepositoryImple implements PromotorRepository {




    private final PromotorMapper promotorMapper;
    private final SimpleJdbcCall saveCall;
    private final SimpleJdbcCall contarCall;
    private final SimpleJdbcCall listarCall;
    private final SimpleJdbcCall findByIdCall;




    public PromotorRepositoryImple(PromotorMapper promotorMapper, @Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.promotorMapper = promotorMapper;
        this.saveCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_CREAR_PROMOTOR)
                .declareParameters(
                        new SqlParameter("nombres", Types.VARCHAR),
                        new SqlParameter("apellidos", Types.VARCHAR),
                        new SqlParameter("doi", Types.VARCHAR),
                        new SqlParameter("correo", Types.VARCHAR),
                        new SqlParameter("idInmobiliaria", Types.BIGINT),
                        new SqlParameter("idAdminEncargado", Types.VARCHAR),
                        new SqlParameter("estado", Types.BIT),
                        new SqlParameter("fechaCreacion", Types.TIMESTAMP),
                        new SqlParameter("fechaModificacion", Types.TIMESTAMP)
                )
                .returningResultSet("promotor", new RowMapperPromotor());
        this.contarCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_CONTAR_PROMOTORES)
                .declareParameters(new SqlParameter("idAdminCreador", Types.VARCHAR))
                .returningResultSet("totalItems", (rs, rowNum) -> rs.getLong("totalItems"));
        this.listarCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_LISTAR_PROMOTORES)
                .declareParameters(
                        new SqlParameter("idAdminCreador", Types.VARCHAR),
                        new SqlParameter("PageNumber", Types.INTEGER),
                        new SqlParameter("PageSize", Types.INTEGER)
                ).returningResultSet("promotores", new RowMapperDashBoardPromotor());
        this.findByIdCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_BUSCAR_PROMOTOR_ID)
                .declareParameters(new SqlParameter("idUsuario", Types.BIGINT))
                .returningResultSet("promotor", new RowMapperPromotor());
    }


    @Override
    public Promotor crear(Promotor promotor) {
        PromotorEntity entity = promotorMapper.toEntity(promotor);
        Map<String, Object> params = new HashMap<>();
        params.put("nombres", entity.getNombres());
        params.put("apellidos", entity.getApellidos());
        params.put("doi", entity.getDoi());
        params.put("correo", entity.getCorreo());
        params.put("idInmobiliaria", entity.getIdInmobiliaria());
        params.put("idAdminEncargado", entity.getIdAdminEncargado());
        params.put("estado", entity.isEstado());
        params.put("fechaCreacion", entity.getFechaCreacion());
        params.put("fechaModificacion", entity.getFechaModificacion());
        Map<String, Object> result = saveCall.execute(params);
        List<PromotorEntity> list = (List<PromotorEntity>) result.get("promotor");
        if (list == null || list.isEmpty()) {
            throw new RuntimeException("Error: El SP 'sp_crear_promotor' no devolvió la fila creada.");
        }
        return promotorMapper.toDomain(list.get(0));

    }

    @Override
    public Optional<Promotor> buscarPorId(Long idPromotor) {
        Map<String, Object> params = Map.of("idUsuario", idPromotor);
        Map<String, Object> result = findByIdCall.execute(params);

        @SuppressWarnings("unchecked")
        List<PromotorEntity> entityList = (List<PromotorEntity>) result.get("promomotores");


        if (entityList == null || entityList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(promotorMapper.toDomain(entityList.get(0)));
    }

    @Override
    public Optional<Promotor> actualizar(Promotor promotor) {
        return Optional.empty();
    }

    @Override
    public long contarPorAdmin(String idAdminCreador) {
        Map<String, Object> params = new HashMap<>();
        params.put("idAdminCreador", idAdminCreador);
        Map<String, Object> resultado = this.contarCall.execute(params);

        List<Long> totalList = (List<Long>) resultado.get("totalItems");
        return totalList == null || totalList.isEmpty() ? 0 : totalList.get(0);
    }

    @Override
    public List<PromotorDashBoard> listarPorAdmin(String idAdminCreador, int page, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("idAdminCreador", idAdminCreador);
        params.put("PageNumber", page);
        params.put("PageSize", size);

        Map<String, Object> resultado = this.listarCall.execute(params);

        @SuppressWarnings("unchecked")
        List<PromotorDashBoard> list = (List<PromotorDashBoard>) resultado.get("promotores");
        return list != null ? list : List.of();
    }
}
