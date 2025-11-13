package com.example.demo.infraestructure.persistence.repositorys;

import com.example.demo.domain.entities.AsesorExterno;
import com.example.demo.domain.repository.query.AsesorRepositoryQuery;
import com.example.demo.infraestructure.persistence.constants.StoredProcedureConstants;
import com.example.demo.infraestructure.persistence.entities.AsesorExternoEntity;
import com.example.demo.infraestructure.persistence.mapper.AsesorExternoMapper;
import com.example.demo.infraestructure.persistence.mapper.RowMapperAsesorExterno;
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
public class AsesorExternoRepositoryImpl implements AsesorRepositoryQuery {

    private final AsesorExternoMapper mapper;

    private final SimpleJdbcCall listarCall;
    private final SimpleJdbcCall contarCall;

    public AsesorExternoRepositoryImpl(
            AsesorExternoMapper mapper,
            @Qualifier("legacyJdbcTemplate") JdbcTemplate legacyJdbcTemplate
    ) {
        this.mapper = mapper;
        this.listarCall = new SimpleJdbcCall(legacyJdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_LISTAR_ASESORES) // (Usa tu constante)
                .declareParameters(
                        new SqlParameter("nombre", Types.VARCHAR),
                        new SqlParameter("ciudad", Types.VARCHAR),
                        new SqlParameter("PageNumber", Types.INTEGER),
                        new SqlParameter("PageSize", Types.INTEGER)
                )
                .returningResultSet("asesores", new RowMapperAsesorExterno()); // Traductor 1
        this.contarCall = new SimpleJdbcCall(legacyJdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_CONTAR_ASESORES) // (Usa tu constante)
                .declareParameters(
                        new SqlParameter("nombre", Types.VARCHAR),
                        new SqlParameter("ciudad", Types.VARCHAR)
                )
                .returningResultSet("totalItems", (rs, rowNum) -> rs.getLong("totalItems"));
    }


    @Override
    public List<AsesorExterno> listarAsesores(String nombre, String ciudad, int page, int size) {

        Map<String, Object> params = new HashMap<>();
        params.put("nombre", nombre);
        params.put("ciudad", ciudad);
        params.put("PageNumber", page);
        params.put("PageSize", size);

        Map<String, Object> resultado = this.listarCall.execute(params);

        List<AsesorExternoEntity> entidadList = (List<AsesorExternoEntity>) resultado.get("asesores");
        return mapper.toDomainList(entidadList);


    }

    @Override
    public long contarAsesores(String nombre, String ciudad) {

        Map<String, Object> params = new HashMap<>();
        params.put("nombre", nombre);
        params.put("ciudad", ciudad);
        // ¡Usa la llamada pre-configurada!
        Map<String, Object> resultado = this.contarCall.execute(params);

        List<Long> totalList = (List<Long>) resultado.get("totalItems");
        return totalList == null || totalList.isEmpty() ? 0 : totalList.get(0);
    }
    }


