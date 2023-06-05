package org.gnori.votingsystemrest.config;

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
        title = "voting-system-rest",
        contact = @Contact(
            name = "gnori",
            url = "https://github.com/gnori-zon"
        ),
        description = "OpenApi documentation for voting-system-rest",
        version = "1.0",
        license = @License(
            name = "MIT",
            url = "https://github.com/gnori-zon/voting-system-rest/blob/master/LICENSE"
        )
    ),
    servers = @Server(
        url = "http://localhost:8080",
        description = "Local ENV"
    ),
    security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT auth with bearer header",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

}
