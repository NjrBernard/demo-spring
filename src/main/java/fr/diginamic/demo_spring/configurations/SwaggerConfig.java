package fr.diginamic.demo_spring.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Liste villes")
                        .version("1.0")
                        .description("Cette API fournit des informations sur les villes de France.")
                        .termsOfService("OPEN DATA")
                        .contact(new Contact().name("Nicolas").email("nico@email.com").url("URL du contact"))
                        .license(new License().name("Nom de la licence").url("URL de la licence")));
    }
}