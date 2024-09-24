package com.tracker.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Raj Aryan
 * Created on 24/09/24.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title("Job Tracker")
                        .description("Easy way to track your Job Applications")
                        .contact(new Contact().name("Pawan Saini").email("imRajAryan09@gmail.com")));
    }
}
