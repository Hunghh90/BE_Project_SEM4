package com.example.beprojectsem4.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//import springfox.documentation.spi.DocumentationType;

@Configuration
//@EnableSwagger2
public class SwagerConfig {
    @Bean
    public GroupedOpenApi controllerApi() {
        return GroupedOpenApi.builder()
                .group("controller-api")
                .packagesToScan("com.example.beprojectsem4.controller") // Specify the package to scan
                .build();
    }
//    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.example.beprojectsem4.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }
}
