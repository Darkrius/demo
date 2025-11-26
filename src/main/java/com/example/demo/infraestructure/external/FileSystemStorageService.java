package com.example.demo.infraestructure.external;

import com.example.demo.application.dto.UploadFileCommand;
import com.example.demo.application.exceptions.StorageException;
import com.example.demo.application.interfaces.external.IStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Slf4j
public class FileSystemStorageService implements IStorageService {

    private final Path rootLocation;
    private static final long MAX_SIZE_BYTES = 5L * 1024 * 1024;
    private static final List<String> ALLOWED_MIME_TYPES = List.of("application/pdf");

    public FileSystemStorageService(@Value("${storage.root}") String rootDirectory) {
        this.rootLocation = Paths.get(rootDirectory);

        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("No se pudo inicializar el almacenamiento", e);
        }
    }

    @Override
    public String subirArchivo(UploadFileCommand archivo, String carpetaDestino) {

        validarNombreArchivo(archivo);
        validarMimeType(archivo);
        validarTamano(archivo);
        try {
            String nombreUnico = UUID.randomUUID() + "-" + archivo.filename();

            // 3. Definir ruta final: C:/archivos/logos-inmobiliarias/uuid-logo.png
            Path rutaDestino = this.rootLocation.resolve(carpetaDestino);
            Files.createDirectories(rutaDestino); // Asegura que la subcarpeta exista

            Path archivoFinal = rutaDestino.resolve(nombreUnico);

            Files.copy(archivo.contentStream(), archivoFinal, StandardCopyOption.REPLACE_EXISTING);

            log.info("INFRA STORAGE: Archivo guardado en: {}", archivoFinal.toAbsolutePath());

            // 5. Retornar la "URL" (Simulada)
            // En S3 devolverías: https://bucket.aws.com/...
            // Aquí devolvemos la ruta relativa para guardarla en BD
            return "/uploads/" + carpetaDestino + "/" + nombreUnico;

        } catch (IOException e) {
            log.error("INFRA STORAGE ERROR: Falló la escritura en disco.", e);
            throw new StorageException("Error al guardar el archivo.", e);
        }
    }

    private void validarNombreArchivo(UploadFileCommand archivo) {
        if (archivo.filename() == null || archivo.filename().isEmpty()) {
            throw new StorageException("Nombre de archivo inválido.");
        }
        String filename = StringUtils.cleanPath(archivo.filename());
        if (filename.contains("..")) {
            throw new StorageException("El nombre del archivo contiene rutas inválidas.");
        }
    }
    private void validarTamano(UploadFileCommand archivo) {
        if (archivo.size() > MAX_SIZE_BYTES) {
            long sizeMb = archivo.size() / (1024L * 1024L);
            throw new StorageException("Archivo excede 5MB. Actual: " + sizeMb + "MB");
        }
    }

    private void validarMimeType(UploadFileCommand archivo) {
        if (!ALLOWED_MIME_TYPES.contains(archivo.contentType())) {
            throw new StorageException(
                    "Tipo no permitido [" + archivo.contentType() + "]"
            );
        }
    }
}
