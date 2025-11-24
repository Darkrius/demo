package com.example.demo.application.services;

import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.queries.AsesorDashBoardDto;
import com.example.demo.application.interfaces.usecases.ListarAsesorService;
import com.example.demo.domain.dto.AsesorDashBoard;
import com.example.demo.domain.model.Asesores;
import com.example.demo.domain.repository.AsesorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ListarAsesoresServiceImpl implements ListarAsesorService {

    private final AsesorRepository asesorRepository;

    public ListarAsesoresServiceImpl(AsesorRepository asesorRepository) {
        this.asesorRepository = asesorRepository;
    }


    @Transactional(readOnly = true)
    @Override
    public PaginationResponseDTO<AsesorDashBoardDto> listarAsesoresGestion(int page, int size, String nombre) {

        log.info("SERVICE: Listando Asesores Internos (Dashboard). Page: [{}], Size: [{}], Filtro Nombre: [{}]", page, size, nombre);
        //tremos nuestra lista enriquecida
        List<AsesorDashBoard> listaDominio = asesorRepository.buscarPorNombreParcial(nombre, page, size);
        //contamos los elementos
        long totalElementos = listaDominio.isEmpty() ? 0 : listaDominio.get(0).totalRegistros();
        log.debug("SERVICE: Repositorio retornó [{}] elementos en esta página. Total global en BD: [{}]", listaDominio.size(), totalElementos);
        //mapeamos nuestro campo que trae el total y aqui ya no lo mostramos
        List<AsesorDashBoardDto> contenidoDto = listaDominio.stream()
                .map(domainObj -> new AsesorDashBoardDto(
                        domainObj.idAsesor(),
                        domainObj.nombreCompleto(),
                        domainObj.nombreCiudad(),
                        domainObj.tipoReferido()
                ))
                .toList();


        // calculamos la paginacion
        int totalPages = (size > 0) ? (int) Math.ceil((double) totalElementos / size) : 0;
        boolean isLast = page >= totalPages;

        log.info("SERVICE: Dashboard generado  elementos.");
        //devolvemos
        return new PaginationResponseDTO<>(
                contenidoDto,
                page,
                size,
                totalElementos,
                totalPages,
                isLast
        );
    }
}
