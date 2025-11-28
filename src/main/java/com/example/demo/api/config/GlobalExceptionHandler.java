package com.example.demo.api.config;

import com.example.demo.application.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.demo.api.dto.ErrorResponse;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler({ AccessDeniedException.class, AuthorizationDeniedException.class })
    public ResponseEntity<ErrorResponse> handleAccessDenied(Exception ex) {
        log.error(" ocurrido un error inesperado.", ex);
        return buildResponse(HttpStatus.FORBIDDEN, "No tiene permisos para realizar esta acción.");
    }

    @ExceptionHandler(ReglasNegocioException.class)
    public ResponseEntity<ErrorResponse> handleReglaNegocio(ReglasNegocioException ex) {
        log.error("Ha  un error inesperado.", ex);
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(RecursoNoEncontradoException ex) {
        log.error("Ha ocurrido  error inesperado.", ex);
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 2. ENTIDAD DUPLICADA -> 409 Conflict
    @ExceptionHandler(EntidadDuplicadaException.class)
    public ResponseEntity<ErrorResponse> handleDuplicado(EntidadDuplicadaException ex) {
        log.error("Ha ocurrido un  inesperado.", ex);
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // 3. ERROR EXTERNO -> 502 Bad Gateway (O 503 Service Unavailable)
    @ExceptionHandler(ErrorDeConexionExternaException.class)
    public ResponseEntity<ErrorResponse> handleExterno(ErrorDeConexionExternaException ex) {
        log.error("Ha ocurrido un error .", ex);
        return buildResponse(HttpStatus.BAD_GATEWAY, "El servicio de asesores externos no está disponible momentáneamente.");
    }

    @ExceptionHandler({StorageException.class, ErrorInesperadoException.class,EventPublishingException.class, Exception.class})
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        log.error("API 500 CRITICO: Ha ocurrido un error inesperado.", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error interno al procesar la solicitud.");
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ErrorResponse> handleSql(PersistenceException ex) {
        log.error("Ha ocurrido un error inesperado.", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error al acceder a la base de datos.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Datos inválidos");

        return buildResponse(HttpStatus.BAD_REQUEST, msg);
    }

    // Helper para no repetir código
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String msg) {
        ErrorResponse error = new ErrorResponse(String.valueOf(status.value()), msg, LocalDateTime.now());
        return ResponseEntity.status(status).body(error);
    }
}
