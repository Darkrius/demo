package com.example.demo.infraestructure.persistence.repositorys;

import com.example.demo.domain.repository.query.UbigeoRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class UbigeoRepositoryImpl implements UbigeoRepository {

    private static final Logger log = LoggerFactory.getLogger(UbigeoRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcCall simpleJdbcCall;

    public UbigeoRepositoryImpl(@Qualifier("jdbcTemplate")JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        String dbName = jdbcTemplate.queryForObject("SELECT DB_NAME()", String.class);
        log.info("Conectado a la base de datos: {}", dbName);
        this.simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("dbo.sp_get_ubigeo_by_distrito")
                .withoutProcedureColumnMetaDataAccess() // importante
                .declareParameters(new SqlParameter("distrito", java.sql.Types.VARCHAR))
                .returningResultSet("result", (rs, rowNum) -> rs.getLong("idUbigeo"));

    }

    @Override
    public Long findIdUbigeoByDistrito(String distrito) {
        Map<String, Object> inParams = Map.of("distrito", distrito);
        Map<String, Object> out = simpleJdbcCall.execute(inParams);

        @SuppressWarnings("unchecked")
        List<Long> resultList = (List<Long>) out.get("result");
        return resultList.isEmpty() ? null : resultList.get(0);

    }
}
