package com.example.demo.infraestructure.external;

import org.springframework.stereotype.Component;

@Component
public class AuthServiceAdapter   {
//
//    private final AuthServicePortFeing authServicePortFeing;
//
//    public AuthServiceAdapter(AuthServicePortFeing authServicePortFeing) {
//        this.authServicePortFeing = authServicePortFeing;
//    }
//
//    @Override
//    public void provisionarUsuario(String correo, String passwordTemporal   ) {
//
//        ProvisionPromotorRequest requestBody = new ProvisionPromotorRequest(correo, passwordTemporal);
//
//        try {
//            authServicePortFeing.provisionarUsuario(requestBody);
//            System.out.println("INFO: Usuario provisionado exitosamente en auth-service para: " + correo);
//        } catch (Exception e) {
//            System.err.println("ERROR: Fallo al provisionar usuario vía Feign [" + correo + "]: " + e.getMessage());
//            e.printStackTrace();
//            throw new RuntimeException("Fallo al contactar servicio de autenticación", e);
//        }
//    }
}
