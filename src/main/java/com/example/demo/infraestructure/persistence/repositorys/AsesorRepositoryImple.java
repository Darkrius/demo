package com.example.demo.infraestructure.persistence.repositorys;

import com.example.demo.domain.entities.Asesores;
import com.example.demo.domain.repository.command.AsesorRepositoryCommand;
import com.example.demo.domain.repository.query.UbigeoRepository;
import com.example.demo.infraestructure.persistence.mapper.AsesorInternoMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AsesorRepositoryImple implements AsesorRepositoryCommand {

    private final SimpleJdbcCall saveCall;
    private final AsesorInternoMapper mapper;
    private final UbigeoRepository ubigeoRepository;

    public AsesorRepositoryImple(JdbcTemplate jdbcTemplate,
                                AsesorInternoMapper mapper,
                                UbigeoRepository ubigeoRepository) {
        this.mapper = mapper;
        this.ubigeoRepository = ubigeoRepository;
        this.saveCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_configurar_asesor")
                .declareParameters(
                        new SqlParameter("idAsesor", Types.VARCHAR),
                        new SqlParameter("nombres", Types.VARCHAR),
                        new SqlParameter("apellidos", Types.VARCHAR),
                        new SqlParameter("correoCorporativo", Types.VARCHAR),
                        new SqlParameter("tipoReferido", Types.VARCHAR),
                        new SqlParameter("estado", Types.BIT),
                        new SqlParameter("fechaUltimaAsignacion", Types.TIMESTAMP),
                        new SqlParameter("idUbigeo", Types.BIGINT)
                );
    }


    @Override
    public void saveAll(List<Asesores> asesores) {
        for (Asesores a : asesores) {
            Map<String, Object> params = new HashMap<>();
            params.put("idAsesor", a.getIdAsesor());
            params.put("nombres", a.getNombres());
            params.put("apellidos", a.getApellidos());
            params.put("correoCorporativo", a.getCorreoCorporativo());
            params.put("tipoReferido", a.getTipoReferido());
            params.put("estado", a.isEstado());
            params.put("fechaUltimaAsignacion", a.getFechaUltimaAsignacion());
            params.put("idUbigeo", a.getIdUbigeo());
            saveCall.execute(params);
        }
    }
}
