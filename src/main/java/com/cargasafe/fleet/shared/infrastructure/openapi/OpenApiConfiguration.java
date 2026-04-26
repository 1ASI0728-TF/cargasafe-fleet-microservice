package com.cargasafe.fleet.shared.infrastructure.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI fleetServiceOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("CargaSafe Fleet Service API")
                        .version("1.0.0")
                        .description("Fleet management microservice — Vehicles & IoT Devices"));
    }
}
