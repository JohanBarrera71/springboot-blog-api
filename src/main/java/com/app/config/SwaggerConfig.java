package com.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "API BLOGS",
                description = "Our blog application is a platform designed to allow users to create, manage, and share their ideas and thoughts through blog posts. With a focus on authentication and social interaction, our app provides a secure and friendly environment where users can freely express themselves and connect with others.",
                termsOfService = "https//:services-and-terms",
                version = "1.0.0",
                contact = @Contact(
                        name = "Johan Barrera",
                        url = "https//johan-web",
                        email = "johan.dev.71@gmail.com"
                ),
                license = @License(
                        name = "Standard Software Use License for JohanDev",
                        url = "https//johan-web"
                )
        ),
        servers = {
                @Server(
                        description = "Dev Server",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Prod Server",
                        url = "http://johan-dev:8080"
                )
        },
        security = @SecurityRequirement(
                name = "Security Token"
        )
)
@SecurityScheme(
        name = "Security Token",
        description = "Access Token For My API",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
