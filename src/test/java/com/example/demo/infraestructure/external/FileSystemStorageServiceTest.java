package com.example.demo.infraestructure.external;

import com.example.demo.application.dto.UploadFileCommand;
import com.example.demo.application.exceptions.StorageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
class FileSystemStorageServiceTest {

    @TempDir
    Path tempDir;

    private FileSystemStorageService storageService;

    @BeforeEach
    void setUp() {
        storageService = new FileSystemStorageService(tempDir.toString());
    }

    private UploadFileCommand crearArchivoSimulado(String nombre, String mime, long sizeBytes) {
        InputStream content = new ByteArrayInputStream("contenido-dummy".getBytes());
        return new UploadFileCommand(nombre, mime, sizeBytes, content);
    }


    @Test
    @DisplayName("Debe guardar correctamente un PDF válido")
    void subirArchivo_Exito() {
        // Given
        UploadFileCommand archivo = crearArchivoSimulado("contrato.pdf", "application/pdf", 1024);
        String carpetaDestino = "contratos";

        // When
        String resultadoUrl = storageService.subirArchivo(archivo, carpetaDestino);

        // Then
        assertNotNull(resultadoUrl);
        assertTrue(resultadoUrl.contains("contrato.pdf"));
        assertTrue(resultadoUrl.startsWith("/uploads/contratos/"));

        // Verificación física: ¿El archivo realmente se creó en el disco temporal?
        // El nombre tiene un UUID aleatorio, así que buscamos en la carpeta
        Path carpetaReal = tempDir.resolve(carpetaDestino);
        assertTrue(Files.exists(carpetaReal), "La carpeta destino debería existir");
        // Debería haber 1 archivo dentro
        assertDoesNotThrow(() -> assertEquals(1, Files.list(carpetaReal).count()));
    }

    // -------------------------------------------------------
    // TEST 2: VALIDACIÓN MIME TYPE
    // -------------------------------------------------------
    @Test
    @DisplayName("Debe fallar si el archivo no es PDF")
    void validarMimeType_Fallo() {
        // Given: Un archivo PNG
        UploadFileCommand archivo = crearArchivoSimulado("foto.png", "image/png", 1024);

        // When & Then
        StorageException ex = assertThrows(StorageException.class, () -> storageService.subirArchivo(archivo, "fotos"));

        assertTrue(ex.getMessage().contains("Tipo no permitido"));
    }

    // -------------------------------------------------------
    // TEST 3: VALIDACIÓN TAMAÑO
    // -------------------------------------------------------
    @Test
    @DisplayName("Debe fallar si el archivo excede 5MB")
    void validarTamano_Fallo() {
        // Given: 6MB (6 * 1024 * 1024)
        long sizeGigante = 6L * 1024 * 1024;
        UploadFileCommand archivo = crearArchivoSimulado("grande.pdf", "application/pdf", sizeGigante);

        // When & Then
        StorageException ex = assertThrows(StorageException.class, () -> storageService.subirArchivo(archivo, "docs"));

        assertTrue(ex.getMessage().contains("Archivo excede 5MB"));
    }

    // -------------------------------------------------------
    // TEST 4: SEGURIDAD (PATH TRAVERSAL)
    // -------------------------------------------------------
    @Test
    @DisplayName("Debe fallar si el nombre intenta salir del directorio (..)")
    void validarNombre_PathTraversal() {
        // Given: Nombre malicioso
        UploadFileCommand archivo = crearArchivoSimulado("../hack.exe", "application/pdf", 1024);

        // When & Then
        StorageException ex = assertThrows(StorageException.class, () -> storageService.subirArchivo(archivo, "docs"));

        assertTrue(ex.getMessage().contains("rutas inválidas"));
    }



}
