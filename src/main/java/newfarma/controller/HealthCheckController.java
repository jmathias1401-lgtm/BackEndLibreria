package newfarma.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/check")
@Tag(name = "Health Check", description = "Endpoints de monitoreo de disponibilidad y salud del servicio")
public class HealthCheckController {

    @Operation(summary = "Chequeo básico de salud", description = "Retorna OK si la aplicación se encuentra en ejecución")
    @ApiResponse(responseCode = "200", description = "Servicio en línea")
    @SecurityRequirements
    @GetMapping
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("OK");
    }

    @Operation(summary = "Chequeo /health", description = "Endpoint de comprobación para Docker o Kubernetes")
    @ApiResponse(responseCode = "200", description = "Servicio en línea")
    @SecurityRequirements
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    @Operation(summary = "Chequeo /healthcheck", description = "Endpoint alternativo de comprobación")
    @ApiResponse(responseCode = "200", description = "Servicio en línea")
    @SecurityRequirements
    @GetMapping("/healthcheck")
    public ResponseEntity<String> healthcheck() {
        return ResponseEntity.ok("OK");
    }

    @Operation(summary = "Chequeo de raíz", description = "Mensaje informativo del estado de la API")
    @ApiResponse(responseCode = "200", description = "API funcionando correctamente")
    @SecurityRequirements
    @GetMapping("/")
    public ResponseEntity<String> root() {
        return ResponseEntity.ok("API funcionando correctamente");
    }
}
