package com.example.pdponline.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "PDP ONLINE", version = "v1",
                description = "This API just for learning Spring boot features",
                contact = @Contact(name = "Sfera IT Solution", url = "http://sferaacedmy.uz", email = "admin@gmail.com"),
                license = @License(name = "Apache Foundation", url = "https://apache.org/")
        ),
        security = {
                @SecurityRequirement(  name = "Bearer")
        }
)
@SecurityScheme(
        name = "Bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "Bearer"
)
public class OpenApiConfig {
}
