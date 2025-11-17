package com.example.demo.infraestructure.persistence.repositorys;

import com.example.demo.domain.entities.Promotor;
import com.example.demo.domain.repository.PromotorRepository;
import com.example.demo.infraestructure.persistence.constants.StoredProcedureConstants;
import com.example.demo.infraestructure.persistence.entities.AsesorExternoEntity;
import com.example.demo.infraestructure.persistence.entities.PromotorEntity;
import com.example.demo.infraestructure.persistence.mapper.PromotorMapper;
import com.example.demo.infraestructure.persistence.mapper.RowMapperInmobiliariaDash;
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





    //implementar los sp y la logica para crar aun nose si esta bien o no xd.


    public PromotorRepositoryImple(PromotorMapper promotorMapper, @Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.promotorMapper = promotorMapper;
        this.saveCall = new SimpleJdbcCall(jdbcTemplate);
        this.contarCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_CONTAR_PROMOTORES)
                .declareParameters(new SqlParameter("idAdminCreador", Types.VARCHAR))
                .returningResultSet("totalItems", (rs, rowNum) -> rs.getLong("totalItems"));
        this.listarCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_LISTAR_PROMOTORES)
                .declareParameters(
                        new SqlParameter("@idAdminCreador", Types.VARCHAR),
                        new SqlParameter("PageNumber", Types.INTEGER),
                        new SqlParameter("PageSize", Types.INTEGER)
                ).returningResultSet("promotores", new RowMapperInmobiliariaDash());
    }


    @Override
    public Promotor crear(Promotor promotor) {
        return null;
    }

    @Override
    public Optional<Promotor> buscarPorId(Long idPromotor) {
        return Optional.empty();
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
    public List<Promotor> listarPorAdmin(String idAdminCreador, int page, int size) {
        Map<String, Object> params = new HashMap<>();
        params.put("idAdminCreador", idAdminCreador);
        params.put("PageNumber", page);
        params.put("PageSize", size);

        Map<String, Object> resultado = this.listarCall.execute(params);

        List<PromotorEntity> entidadList = (List<PromotorEntity>) resultado.get("promotores");
        return promotorMapper.toDomainList(entidadList);
    }
}
