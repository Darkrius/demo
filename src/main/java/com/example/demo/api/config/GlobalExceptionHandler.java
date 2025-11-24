package com.example.demo.api.config;

import com.example.demo.application.exceptions.EntidadDuplicadaException;
import com.example.demo.application.exceptions.ErrorDeConexionExternaException;
import com.example.demo.application.exceptions.RecursoNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.demo.api.dto.ErrorResponse;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(RecursoNoEncontradoException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 2. ENTIDAD DUPLICADA -> 409 Conflict
    // Ej: Intentas registrar un asesor con ID que ya existe.
    @ExceptionHandler(EntidadDuplicadaException.class)
    public ResponseEntity<ErrorResponse> handleDuplicado(EntidadDuplicadaException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // 3. ERROR EXTERNO -> 502 Bad Gateway (O 503 Service Unavailable)
    // Ej: La base de datos Legacy no responde.
    @ExceptionHandler(ErrorDeConexionExternaException.class)
    public ResponseEntity<ErrorResponse> handleExterno(ErrorDeConexionExternaException ex) {
        return buildResponse(HttpStatus.BAD_GATEWAY, "El servicio de asesores externos no está disponible momentáneamente.");
    }

    // Helper para no repetir código
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String msg) {
        ErrorResponse error = new ErrorResponse(String.valueOf(status.value()), msg, LocalDateTime.now());
        return ResponseEntity.status(status).body(error);
    }
}
