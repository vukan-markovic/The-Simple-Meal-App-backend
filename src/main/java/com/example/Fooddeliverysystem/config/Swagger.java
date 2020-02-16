package com.example.Fooddeliverysystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class Swagger {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.example.Fooddeliverysystem.controller"))
                .paths(PathSelectors.regex("/.*"))
                .build()
                .apiInfo(metaInfo());

    }

    private ApiInfo metaInfo() {
        return new ApiInfo("Simple Meal API", "", "", "http://localhost:8080",
                new Contact("Simple TAsk", "", ""), "",
                "");
    }
}