package com.example.demo.infraestructure.external;


import com.example.demo.application.dto.ProvisionPromotorRequest;
import com.example.demo.application.dto.ProvisionPromotorResponse;
import feign.RequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "localhost:8088", name = "auth-service", configuration = AuthServicePortFeing.FeignConfig.class)
public interface AuthServicePortFeing {


    @PostMapping("/api/auth/provision/promotor")
    ProvisionPromotorResponse provisionarUsuario (@RequestBody ProvisionPromotorRequest request);

    class FeignConfig {
        @Bean
        public RequestInterceptor requestInterceptor() {
            return requestTemplate -> {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
                    requestTemplate.header("Authorization", "Bearer " + jwt.getTokenValue());
                }
            };
        }
    }
}
