package br.com.alefeoliveira.core.config;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class OpenApiConfig {
	
	 @Bean
	 public OpenAPI customOpenAPI() {
		 //OpenAPI()
         //.components(new Components().addSchemas("SeuModeloAdicional", 
         //        new Schema<>().$ref("#/components/schemas/SeuModeloAdicional")));
	        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Minha API")
                        .description("Descrição da minha API")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Seu Nome")
                                .email("seu.email@dominio.com")
                                .url("https://seusite.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .tags(Arrays.asList(new Tag().name("Cidades").description("Gerencia as cidades")));
	 }
}
