package com.example.demo.application.dto;

import java.io.InputStream;

public record UploadFileCommand(
        String filename,
        String contentType,
        long size,
        InputStream contentStream
) {
}
