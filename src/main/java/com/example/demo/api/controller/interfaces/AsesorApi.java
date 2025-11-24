package com.example.demo.api.controller.interfaces;

import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.commands.RegistrarAsesorCommand;
import com.example.demo.application.dto.queries.AsesorDashBoardDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/asesores")
@Tag(name = "Gestión de Asesores", description = "Endpoints para listar candidatos externos y gestionar asesores internos.")
public interface AsesorApi {

    @Operation(summary = "Listar Asesores (Unificado)",
            description = "Permite visualizar la lista de asesores. Usa el parámetro 'origen' para alternar entre candidatos (EXTERNO) y asesores gestionados (INTERNO).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    ResponseEntity<PaginationResponseDTO<?>> listar(
            @Parameter(description = "Número de página (empieza en 1)", example = "1")
            @RequestParam(defaultValue = "1") int page,

            @Parameter(description = "Cantidad de elementos por página", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Filtro por nombre o apellido (parcial)")
            @RequestParam(required = false) String nombre,

            @Parameter(description = "Filtro por ciudad (Solo aplica para origen EXTERNO)")
            @RequestParam(required = false) String ciudad,

            @Parameter(description = "Fuente de datos: 'INTERNO' (Dashboard) o 'EXTERNO' (Legacy)", example = "INTERNO")
            @RequestParam(defaultValue = "INTERNO") String origen
    );

    @Operation(summary = "Registrar / Importar Asesor",
            description = "Crea un nuevo asesor en la base de datos de gestión. Valida que la ciudad exista y que el asesor no esté duplicado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Asesor creado exitosamente",
                    content = @Content(schema = @Schema(implementation = AsesorDashBoardDto.class))),
            @ApiResponse(responseCode = "404", description = "Ciudad no encontrada (RecursoNoEncontradoException)", content = @Content),
            @ApiResponse(responseCode = "409", description = "El asesor ya existe (EntidadDuplicadaException)", content = @Content)
    })
    @PostMapping
    ResponseEntity<AsesorDashBoardDto> registrar(
            @Parameter(description = "Datos del asesor a registrar", required = true)
            @RequestBody RegistrarAsesorCommand command
    );


}
