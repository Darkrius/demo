package com.example.demo.domain.repository;

public interface AuthServicePort {

    void provisionarUsuario(String correo, String passwordTemporal);
}
