package newfarma.auth;

import lombok.RequiredArgsConstructor;
import newfarma.auth.dto.TokenResponse;
import newfarma.auth.dto.LoginRequest;
import newfarma.model.Usuario;
import newfarma.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody final Usuario usuario) {
        final TokenResponse token = service.register(usuario);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody final LoginRequest request) {
        final TokenResponse token = service.loggin(request);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/refresh")
    public TokenResponse refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader) throws IllegalAccessException {
        return service.refreshToken(authHeader);
    }
}
