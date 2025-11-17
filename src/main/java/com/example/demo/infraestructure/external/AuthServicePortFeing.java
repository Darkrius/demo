package com.example.demo.infraestructure.external;


import com.example.demo.application.dto.ProvisionPromotorRequest;
import com.example.demo.application.dto.ProvisionPromotorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "Localhost:8088", name = "auth-service")
public interface AuthServicePortFeing {


    @PostMapping("/api/auth/provision/promotor")
    ProvisionPromotorResponse provisionarUsuario (@RequestBody ProvisionPromotorRequest request);
}
