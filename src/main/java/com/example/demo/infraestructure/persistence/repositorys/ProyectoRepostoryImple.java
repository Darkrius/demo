package com.example.demo.infraestructure.persistence.repositorys;

import com.example.demo.domain.entities.Proyecto;
import com.example.demo.domain.repository.ProyectoRepository;
import com.example.demo.infraestructure.persistence.constants.StoredProcedureConstants;
import com.example.demo.infraestructure.persistence.entities.ProyectosEntity;
import com.example.demo.infraestructure.persistence.mapper.ProyectoMapper;
import com.example.demo.infraestructure.persistence.mapper.RowMapperProyecto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProyectoRepostoryImple implements ProyectoRepository {

    private final ProyectoMapper proyectoMapper;
    private final ObjectMapper objectMapper;
    private final SimpleJdbcCall saveCall;

    public ProyectoRepostoryImple(
            @Qualifier("jdbcTemplate")JdbcTemplate jdbcTemplate,
            ProyectoMapper proyectoMapper,
            ObjectMapper objectMapper
    ) {
        this.proyectoMapper = proyectoMapper;
        this.objectMapper = objectMapper;

        this.saveCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName(StoredProcedureConstants.SP_CREAR_PROYECTOS)
                .withSchemaName("dbo")
                .declareParameters(
                        new SqlParameter("idInmobiliaria", Types.BIGINT),
                        new SqlParameter("proyectosJSON", Types.NVARCHAR)
                )
                .returningResultSet("proyectos", new RowMapperProyecto());

    }


    @Override
    public List<Proyecto> crearTodos(List<Proyecto> proyectos) {
        if (proyectos == null || proyectos.isEmpty()) {
            return List.of();
        }
        Long idInmobiliaria = proyectos.get(0).getIdInmobiliaria();
        String proyectosJSON;
        try {
            List<Map<String, String>> jsonList = proyectos.stream()
                    .map(p -> Map.of("nombre", p.getNombre()))
                    .collect(Collectors.toList());
            proyectosJSON = objectMapper.writeValueAsString(jsonList);
        } catch (Exception e) {
            throw new RuntimeException("Error convirtiendo proyectos a JSON", e);
        }

        Map<String, Object> params = Map.of(
                "idInmobiliaria", idInmobiliaria,
                "proyectosJSON", proyectosJSON
        );

        Map<String, Object> result = saveCall.execute(params);

        @SuppressWarnings("unchecked")
        List<ProyectosEntity> entityList = (List<ProyectosEntity>) result.get("proyectos");

        return proyectoMapper.toDomainList(entityList);
    }

    @Override
    public long contarPorInmobiliaria(Long idInmobiliaria) {
        return 0;
    }

    @Override
    public Optional<Proyecto> buscarPorId(Long idProyecto) {
        return Optional.empty();
    }
}
