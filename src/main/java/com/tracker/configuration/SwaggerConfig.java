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
        return new OpenAPI().info(new Info().title("Security Dashboard Next Gen")
                        .description("This is security dashboard 2.0")
                        .contact(new Contact().name("Pawan Saini").email("pawan.saini@moveinsync.com")));
    }
}
