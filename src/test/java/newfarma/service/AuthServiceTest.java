package newfarma.service;

import newfarma.auth.dto.LoginRequest;
import newfarma.auth.dto.TokenResponse;
import newfarma.model.Cargo;
import newfarma.model.Token;
import newfarma.model.Usuario;
import newfarma.repository.TokenRepository;
import newfarma.usuario.UsuarioRepositoryJPA;
import newfarma.usuario.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - AuthService")
class AuthServiceTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsuarioRepositoryJPA usuarioRepositoryJPA;

    @InjectMocks
    private AuthService authService;

    private Usuario sampleUser;
    private Cargo sampleCargo;

    @BeforeEach
    void setUp() {
        sampleCargo = new Cargo();
        sampleCargo.setIdcargo(1L);
        sampleCargo.setNombrecargo("ADMIN");

        sampleUser = Usuario.builder()
                .idusuario(1L)
                .nombreusuario("juanperez")
                .clave("rawPassword")
                .fechacreacion(new Date())
                .cargo(sampleCargo)
                .build();
    }

    @Nested
    @DisplayName("Operación: register(Usuario)")
    class RegisterTests {

        @Test
        @DisplayName("Debe registrar un usuario exitosamente, encriptar la clave y retornar TokenResponse")
        void register_WhenValidUser_ShouldEncodePasswordAndReturnTokens() {
            // Arrange (Given)
            when(passwordEncoder.encode(any())).thenReturn("encodedPassword123");
            when(usuarioService.save(any(Usuario.class))).thenReturn(sampleUser);
            when(jwtService.generateToken(sampleUser)).thenReturn("access-token-123");
            when(jwtService.generateRefreshToken(sampleUser)).thenReturn("refresh-token-456");

            // Act (When)
            TokenResponse response = authService.register(sampleUser);

            // Assert (Then)
            assertThat(response).isNotNull();
            assertThat(response.AccessToken()).isEqualTo("access-token-123");
            assertThat(response.refreshToken()).isEqualTo("refresh-token-456");

            verify(passwordEncoder, times(1)).encode("rawPassword");
            verify(usuarioService, times(1)).save(any(Usuario.class));
            verify(tokenRepository, times(1)).save(any(Token.class));
        }

        @Test
        @DisplayName("Debe lanzar RuntimeException si el usuario ya existe en base de datos")
        void register_WhenUserAlreadyExists_ShouldThrowRuntimeException() {
            // Arrange (Given)
            when(passwordEncoder.encode(any())).thenReturn("encodedPassword123");
            when(usuarioService.save(any(Usuario.class))).thenReturn(null);

            // Act & Assert (When & Then)
            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                    authService.register(sampleUser)
            );

            assertThat(exception.getMessage()).isEqualTo("User already exists");
            verify(jwtService, never()).generateToken(any());
            verify(tokenRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Operación: loggin(LoginRequest)")
    class LoginTests {

        @Test
        @DisplayName("Debe autenticar credenciales válidas, revocar tokens previos y retornar nuevos tokens")
        void loggin_WhenValidCredentials_ShouldRevokeOldTokensAndReturnNewTokens() {
            // Arrange (Given)
            LoginRequest loginRequest = new LoginRequest("juanperez", "password123");

            Token oldToken = Token.builder()
                    .idtoken(1)
                    .token("old-token")
                    .expired(false)
                    .revoked(false)
                    .build();
            List<Token> existingTokens = new ArrayList<>(List.of(oldToken));

            when(usuarioRepositoryJPA.findByNombreusuario("juanperez")).thenReturn(Optional.of(sampleUser));
            when(jwtService.generateToken(sampleUser)).thenReturn("new-access-token");
            when(jwtService.generateRefreshToken(sampleUser)).thenReturn("new-refresh-token");
            when(tokenRepository.findAllValidTokenByUser(sampleUser.getIdusuario())).thenReturn(existingTokens);

            // Act (When)
            TokenResponse response = authService.loggin(loginRequest);

            // Assert (Then)
            assertThat(response).isNotNull();
            assertThat(response.AccessToken()).isEqualTo("new-access-token");
            assertThat(response.refreshToken()).isEqualTo("new-refresh-token");

            verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
            assertThat(oldToken.isExpired()).isTrue();
            assertThat(oldToken.isRevoked()).isTrue();
            verify(tokenRepository, times(1)).saveAll(existingTokens);
            verify(tokenRepository, times(1)).save(any(Token.class));
        }

        @Test
        @DisplayName("Debe lanzar UsernameNotFoundException si el usuario no es encontrado en base de datos")
        void loggin_WhenUserNotFound_ShouldThrowUsernameNotFoundException() {
            // Arrange (Given)
            LoginRequest loginRequest = new LoginRequest("inexistente", "password123");
            when(usuarioRepositoryJPA.findByNombreusuario("inexistente")).thenReturn(Optional.empty());

            // Act & Assert (When & Then)
            UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                    authService.loggin(loginRequest)
            );

            assertThat(exception.getMessage()).contains("User not found: inexistente");
            verify(jwtService, never()).generateToken(any());
        }
    }

    @Nested
    @DisplayName("Operación: refreshToken(String authHeader)")
    class RefreshTokenTests {

        @Test
        @DisplayName("Debe refrescar token exitosamente cuando el header Bearer es válido")
        void refreshToken_WhenHeaderIsValid_ShouldReturnNewAccessToken() throws Exception {
            // Arrange (Given)
            String authHeader = "Bearer valid.refresh.token";
            String tokenValue = "valid.refresh.token";

            when(jwtService.extractUsername(tokenValue)).thenReturn("juanperez");
            when(usuarioRepositoryJPA.findByNombreusuario("juanperez")).thenReturn(Optional.of(sampleUser));
            when(jwtService.isTokenValid(tokenValue, sampleUser)).thenReturn(true);
            when(jwtService.generateToken(sampleUser)).thenReturn("new-generated-access-token");
            when(tokenRepository.findAllValidTokenByUser(sampleUser.getIdusuario())).thenReturn(Collections.emptyList());

            // Act (When)
            TokenResponse response = authService.refreshToken(authHeader);

            // Assert (Then)
            assertThat(response).isNotNull();
            assertThat(response.AccessToken()).isEqualTo("new-generated-access-token");
            assertThat(response.refreshToken()).isEqualTo(tokenValue);

            verify(tokenRepository, times(1)).save(any(Token.class));
        }

        @Test
        @DisplayName("Debe lanzar IllegalAccessException cuando el header es nulo o no inicia con Bearer")
        void refreshToken_WhenHeaderIsMalformed_ShouldThrowIllegalAccessException() {
            assertThrows(IllegalAccessException.class, () -> authService.refreshToken(null));
            assertThrows(IllegalAccessException.class, () -> authService.refreshToken("Basic dXNlcjpwYXNz"));
            assertThrows(IllegalAccessException.class, () -> authService.refreshToken("TokenSinBearer"));
        }

        @Test
        @DisplayName("Debe lanzar IllegalAccessException cuando el token no contiene username")
        void refreshToken_WhenUsernameIsNull_ShouldThrowIllegalAccessException() {
            String authHeader = "Bearer token.sin.subject";
            when(jwtService.extractUsername("token.sin.subject")).thenReturn(null);

            IllegalAccessException ex = assertThrows(IllegalAccessException.class, () ->
                    authService.refreshToken(authHeader)
            );

            assertThat(ex.getMessage()).isEqualTo("Invalid Refresh Token");
        }

        @Test
        @DisplayName("Debe lanzar IllegalAccessException cuando el token es inválido o expiró")
        void refreshToken_WhenTokenIsInvalid_ShouldThrowIllegalAccessException() {
            String authHeader = "Bearer token.invalido";
            when(jwtService.extractUsername("token.invalido")).thenReturn("juanperez");
            when(usuarioRepositoryJPA.findByNombreusuario("juanperez")).thenReturn(Optional.of(sampleUser));
            when(jwtService.isTokenValid("token.invalido", sampleUser)).thenReturn(false);

            IllegalAccessException ex = assertThrows(IllegalAccessException.class, () ->
                    authService.refreshToken(authHeader)
            );

            assertThat(ex.getMessage()).isEqualTo("Invalid Refresh Token");
        }
    }
}
