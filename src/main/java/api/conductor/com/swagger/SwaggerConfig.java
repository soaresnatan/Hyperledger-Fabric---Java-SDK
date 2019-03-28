package api.conductor.com.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("api.conductor.com"))
                .paths(regex("/api.*"))
                .build()
                .apiInfo(apiInfo());

    }

   private ApiInfo apiInfo() {
       return new ApiInfo(
               "API REST",
               "This API is intended to facilitate the communication of the JAVA SDK with Blockchain",
               "1.0",
               "Terms of service",
               new Contact("Leandro Mendes", "www.conductor.com.br", "leandro.santos1@conductor.com.br"),
               "License of API", "/www.apache.org/licensen.html", Collections.emptyList());
   }
}
