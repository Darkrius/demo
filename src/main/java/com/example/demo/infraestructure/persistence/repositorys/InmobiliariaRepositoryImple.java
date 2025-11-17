package com.example.demo.infraestructure.persistence.repositorys;

import com.example.demo.domain.entities.Inmobiliarias;
import com.example.demo.domain.repository.DashBoardInmobiliaria;
import com.example.demo.domain.repository.InmobiliariaRepository;
import com.example.demo.infraestructure.persistence.constants.StoredProcedureConstants;
import com.example.demo.infraestructure.persistence.entities.AsesorExternoEntity;
import com.example.demo.infraestructure.persistence.entities.InmobiliariasEntity;
import com.example.demo.infraestructure.persistence.mapper.InmobiliariaMapper;
import com.example.demo.infraestructure.persistence.mapper.RowMapperInmobiliaria;
import com.example.demo.infraestructure.persistence.mapper.RowMapperInmobiliariaDash;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InmobiliariaRepositoryImple implements InmobiliariaRepository {

    private final InmobiliariaMapper inmobiliariaMapper;
    private final SimpleJdbcCall crearCall;
    private final SimpleJdbcCall buscarPorRucCall;
    private final SimpleJdbcCall contarPorAdminCall; // <-- Campo nuevo
    private final SimpleJdbcCall listarPorAdminCall;

    public InmobiliariaRepositoryImple(
            @Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate,
            InmobiliariaMapper inmobiliariaMapper
    ) {
        this.inmobiliariaMapper = inmobiliariaMapper;

        this.crearCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_CREAR_Inmobiliarias)
                .withSchemaName("dbo")
                .declareParameters(
                        new SqlParameter("ruc", Types.VARCHAR),
                        new SqlParameter("razonSocial", Types.VARCHAR),
                        new SqlParameter("estado", Types.BIT),
                        new SqlParameter("fechaCreacion", Types.TIMESTAMP),
                        new SqlParameter("fechaModificacion", Types.TIMESTAMP),
                        new SqlParameter("idAdminCreador", Types.VARCHAR)
                )
                .returningResultSet("inmobiliaria", new RowMapperInmobiliaria());

        this.buscarPorRucCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_BUSCAR_RUC_INMOBILIARIA)
                .withSchemaName("dbo")
                .declareParameters(new SqlParameter("ruc", Types.VARCHAR))
                .returningResultSet("inmobiliaria", new RowMapperInmobiliaria());

        this.contarPorAdminCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_CONTAR_INMOBILIARIA)
                .declareParameters(new SqlParameter("idAdminCreador", Types.VARCHAR))
                .returningResultSet("totalItems", (rs, rowNum) -> rs.getLong("totalItems"));
        this.listarPorAdminCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_LISTAR_INMOBILIARIA)
                .declareParameters(
                        new SqlParameter("@idAdminCreador", Types.VARCHAR),
                        new SqlParameter("PageNumber", Types.INTEGER),
                        new SqlParameter("PageSize", Types.INTEGER)
                ).returningResultSet("inmobiliarias", new RowMapperInmobiliariaDash());

    }


    @Override
    public Inmobiliarias crear(Inmobiliarias inmobiliaria) {
        InmobiliariasEntity entity = inmobiliariaMapper.toEntity(inmobiliaria);

        // 2. Preparar parámetros (Usamos HashMap para aceptar Nulos)
        Map<String, Object> params = new HashMap<>();
        params.put("ruc", entity.getRuc());
        params.put("razonSocial", entity.getRazonSocial());
        params.put("estado", entity.isEstado());
        params.put("fechaCreacion", entity.getFechaCreacion());
        params.put("fechaModificacion", entity.getFechaModificacion());
        params.put("idAdminCreador", entity.getIdAdminCreador());

        Map<String, Object> result = this.crearCall.execute(params);

        @SuppressWarnings("unchecked") // (Suprime la advertencia inevitable)
        List<InmobiliariasEntity> list = (List<InmobiliariasEntity>) result.get("inmobiliaria");

        if (list == null || list.isEmpty()) {
            throw new RuntimeException("Error: El SP 'sp_crear_inmobiliaria' no devolvió la fila creada.");
        }

        // 4. Devolver el POJO
        return inmobiliariaMapper.toDomain(list.getFirst());
    }

    @Override
    public Optional<Inmobiliarias> buscarPorRUc(String ruc) {
        Map<String, Object> params = Map.of("ruc", ruc);

        Map<String, Object> result = this.buscarPorRucCall.execute(params);

        @SuppressWarnings("unchecked")
        List<InmobiliariasEntity> entityList = (List<InmobiliariasEntity>) result.get("inmobiliaria");

        if (entityList == null || entityList.isEmpty()) {
            return Optional.empty();
        }

        Inmobiliarias inmo = inmobiliariaMapper.toDomain(entityList.get(0));
        return Optional.of(inmo);
    }

    @Override
    public long contarPorAdmin(String idAdminCreador) {
        Map<String, Object> params = new HashMap<>();
        params.put("idAdminCreador", idAdminCreador);
        Map<String, Object> resultado = this.contarPorAdminCall.execute(params);

        List<Long> totalList = (List<Long>) resultado.get("totalItems");
        return totalList == null || totalList.isEmpty() ? 0 : totalList.get(0);
    }

    @Override
    public Optional<Inmobiliarias> buscarPorId(Long idInmobiliaria) {
        return Optional.empty();
    }

    @Override
    public List<DashBoardInmobiliaria> listarPorAdmin(String idAdminCreador, int page, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("idAdminCreador", idAdminCreador);
        params.put("PageNumber", page);
        params.put("PageSize", size);

        Map<String, Object> resultado = this.listarPorAdminCall.execute(params);

        List<DashBoardInmobiliaria> entidadList = (List<DashBoardInmobiliaria>) resultado.get("inmobiliarias");
        return entidadList;
    }

    @Override
    public Optional<Inmobiliarias> actualizar(Inmobiliarias inmobiliaria) {
        return Optional.empty();
    }
}
