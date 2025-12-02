package com.example.demo.api.controller.interfaces;


import com.example.demo.api.dto.request.InmobiliariaRequest;
import com.example.demo.application.dto.PaginationResponseDTO;
import com.example.demo.application.dto.DatosEmpresaDto;
import com.example.demo.application.dto.queries.InmobiliariaDashBoardDto;
import com.example.demo.application.dto.queries.InmobiliariaDetalleDto;
import com.example.demo.application.dto.queries.PromotorDetalleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/inmobiliarias")
@Tag(name = "Gestión de Inmobiliarias", description = "Registro y administración de empresas inmobiliarias.")
public interface InmobiliariaApi {


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<InmobiliariaDashBoardDto> registrar(
            @Parameter(description = "Datos de la inmobiliaria (JSON)", required = true)
            @RequestPart("data") @Valid InmobiliariaRequest request,
            @Parameter(description = "Archivo del logo (Imagen)", required = false)
            @RequestPart(value = "logo", required = false) MultipartFile logo,
            @Parameter(hidden = true) // <--- Oculta este campo en Swagger
            @AuthenticationPrincipal Jwt jwt
    );


    @Operation(summary = "Consultar RUC", description = "Obtiene la Razón Social desde SUNAT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa encontrada",
                    content = @Content(schema = @Schema(implementation = DatosEmpresaDto.class))),
            @ApiResponse(responseCode = "400", description = "RUC inválido (formato incorrecto)", content = @Content),
            @ApiResponse(responseCode = "404", description = "RUC no encontrado en padrón", content = @Content),
            @ApiResponse(responseCode = "502", description = "Error de conexión externa", content = @Content)
    })
    @GetMapping("/consulta-ruc/{ruc}")
    ResponseEntity<DatosEmpresaDto> consultarRuc(

            @Parameter(description = "RUC de 11 dígitos", required = true, example = "20100000001")
            @PathVariable("ruc")
            @Pattern(regexp = "\\d{11}", message = "El RUC debe tener exactamente 11 dígitos numéricos")
            String ruc
    );


    @GetMapping()
    ResponseEntity<PaginationResponseDTO<InmobiliariaDashBoardDto>> listar(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal Jwt jwt
    );


    @Operation(summary = "Obtener detalles de una inmobiliaria", description = "Devulve todo los datos de la inmobiliara, incluyendo los promotores asignados a sus respectivos proyectos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inmobiliaria Encontrado",
                    content = @Content(schema = @Schema(implementation = PromotorDetalleDto.class))),
            @ApiResponse(responseCode = "404", description = "Inmobiliaria no encontrado", content = @Content)
    })
    @GetMapping("/{idInmobiliaria}")
    ResponseEntity<InmobiliariaDetalleDto> obtenerPorId(
            @Parameter(description = "ID unico de la inmobiliaria", required = true, example = "50")
            @PathVariable("idInmobiliaria") Long idInmobiliaria
    );


}
