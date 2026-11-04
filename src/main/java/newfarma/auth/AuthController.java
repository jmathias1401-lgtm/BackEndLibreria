package newfarma.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import newfarma.auth.dto.LoginRequest;
import newfarma.auth.dto.TokenResponse;
import newfarma.model.Usuario;
import newfarma.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para registro, inicio de sesión y renovación de tokens JWT")
public class AuthController {
    private final AuthService service;

    @Operation(summary = "Registrar nuevo usuario", description = "Crea un nuevo usuario en el sistema y retorna el token JWT inicial")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de registro inválidos")
    })
    @SecurityRequirements
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody final Usuario usuario) {
        final TokenResponse token = service.register(usuario);
        return ResponseEntity.ok(token);
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica las credenciales del usuario y genera los tokens de acceso y refresco")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas o no autorizado")
    })
    @SecurityRequirements
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody final LoginRequest request) {
        final TokenResponse token = service.loggin(request);
        return ResponseEntity.ok(token);
    }

    @Operation(summary = "Refrescar token JWT", description = "Genera un nuevo token de acceso a partir de la cabecera Authorization Bearer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token renovado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "403", description = "Token no válido o expirado")
    })
    @GetMapping("/refresh")
    public TokenResponse refreshToken(
            @Parameter(description = "Token Bearer actual en formato 'Bearer <token>'", required = true)
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader) throws IllegalAccessException {
        return service.refreshToken(authHeader);
    }
}
