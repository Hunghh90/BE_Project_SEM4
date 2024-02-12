package com.example.beprojectsem4.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Project-SEM4",
                description = "DESCRIPTION",
                summary = "SUMMARY",
                termsOfService = "FPT",
                contact = @Contact(
                        name = "FPT",
                        email = "hanhnxth2202040@fpt.edu.vn"
                ),
                license = @License(
                        name = "LICENSE"
                ),
                version = "ver1"
        ),
        servers = {
                @Server(
                        description = "Dev",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Product",
                        url="http://localhost:8080"
                )
        },
        security = @SecurityRequirement(
                name = "auth"
        )
)

@SecurityScheme(
        name = "auth",
        in = SecuritySchemeIn.HEADER,
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "basic",
        description = "security desc"
)
public class SwagerConfig {

}
