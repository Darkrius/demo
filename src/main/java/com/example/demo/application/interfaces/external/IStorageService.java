package com.example.demo.application.interfaces.external;

import com.example.demo.application.dto.UploadFileCommand;

public interface IStorageService {

    String subirArchivo(UploadFileCommand archivo, String carpetaDestino);

}
