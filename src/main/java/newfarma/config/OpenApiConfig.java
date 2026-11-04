package newfarma.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API NewFarma / Librería",
                version = "1.0.0",
                description = "Documentación interactiva de la API REST para el sistema NewFarma / Librería. " +
                        "Incluye operaciones de ventas, compras, productos, clientes, empleados, proveedores y autenticación con JWT.",
                contact = @Contact(
                        name = "Equipo de Desarrollo NewFarma",
                        email = "soporte@newfarma.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        servers = {
                @Server(url = "/", description = "Servidor Actual / Base Path")
        },
        security = {
                @SecurityRequirement(name = "BearerAuth")
        }
)
@SecurityScheme(
        name = "BearerAuth",
        description = "Autenticación basada en token JWT. Ingrese el token JWT directamente.",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}

