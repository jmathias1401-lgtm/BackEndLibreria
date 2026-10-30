package newfarma.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import newfarma.auth.dto.LoginRequest;
import newfarma.auth.dto.TokenResponse;
import newfarma.config.JwtAuthFilter;
import newfarma.config.SecurityConfig;
import newfarma.model.Usuario;
import newfarma.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AuthController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                SecurityFilterAutoConfiguration.class
        },
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAuthFilter.class}
        )
)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Pruebas Unitarias - AuthController")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Nested
    @DisplayName("Operación: POST /api/auth/register")
    class RegisterTests {

        @Test
        @DisplayName("Debe registrar un usuario exitosamente y retornar HTTP 200 con TokenResponse")
        void register_WhenValidUser_ShouldReturn200AndTokenResponse() throws Exception {
            // Arrange (Given)
            String usuarioJson = """
                    {
                        "nombreusuario": "juanperez",
                        "clave": "password123"
                    }
                    """;

            TokenResponse expectedTokenResponse = new TokenResponse("access-token-abc", "refresh-token-xyz");
            when(authService.register(any(Usuario.class))).thenReturn(expectedTokenResponse);

            // Act & Assert (When & Then)
            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(usuarioJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.access_token").value("access-token-abc"))
                    .andExpect(jsonPath("$.refresh_token").value("refresh-token-xyz"));

            verify(authService, times(1)).register(any(Usuario.class));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 Bad Request cuando el body está ausente")
        void register_WhenBodyIsMissing_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(authService, never()).register(any());
        }
    }

    @Nested
    @DisplayName("Operación: POST /api/auth/login")
    class LoginTests {

        @Test
        @DisplayName("Debe autenticar credenciales válidas y retornar HTTP 200 con TokenResponse")
        void authenticate_WhenValidCredentials_ShouldReturn200AndTokenResponse() throws Exception {
            // Arrange (Given)
            LoginRequest loginRequest = new LoginRequest("juanperez", "password123");
            TokenResponse expectedTokenResponse = new TokenResponse("login-jwt-token", "login-refresh-token");

            when(authService.loggin(any(LoginRequest.class))).thenReturn(expectedTokenResponse);

            // Act & Assert (When & Then)
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.access_token").value("login-jwt-token"))
                    .andExpect(jsonPath("$.refresh_token").value("login-refresh-token"));

            verify(authService, times(1)).loggin(any(LoginRequest.class));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 Bad Request cuando el body de login no se proporciona")
        void authenticate_WhenBodyIsMissing_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(authService, never()).loggin(any());
        }
    }

    @Nested
    @DisplayName("Operación: GET /api/auth/refresh")
    class RefreshTokenTests {

        @Test
        @DisplayName("Debe refrescar el token exitosamente con header Authorization válido y retornar HTTP 200")
        void refreshToken_WhenHeaderIsValid_ShouldReturn200AndTokenResponse() throws Exception {
            // Arrange (Given)
            String authHeader = "Bearer sample.refresh.token";
            TokenResponse expectedTokenResponse = new TokenResponse("new-jwt-token", "new-refresh-token");

            when(authService.refreshToken(eq(authHeader))).thenReturn(expectedTokenResponse);

            // Act & Assert (When & Then)
            mockMvc.perform(get("/api/auth/refresh")
                            .header(HttpHeaders.AUTHORIZATION, authHeader))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.access_token").value("new-jwt-token"))
                    .andExpect(jsonPath("$.refresh_token").value("new-refresh-token"));

            verify(authService, times(1)).refreshToken(eq(authHeader));
        }

        @Test
        @DisplayName("Debe retornar HTTP 400 Bad Request cuando el header Authorization está ausente")
        void refreshToken_WhenHeaderIsMissing_ShouldReturn400BadRequest() throws Exception {
            mockMvc.perform(get("/api/auth/refresh"))
                    .andExpect(status().isBadRequest());

            verify(authService, never()).refreshToken(any());
        }

        @Test
        @DisplayName("Debe propagar IllegalAccessException cuando el servicio detecta un token inválido")
        void refreshToken_WhenServiceThrowsIllegalAccessException_ShouldPropagate() throws Exception {
            // Arrange (Given)
            String authHeader = "Bearer invalid.token";
            when(authService.refreshToken(eq(authHeader)))
                    .thenThrow(new IllegalAccessException("INVALID BEARER TOKEN"));

            // Act & Assert (When & Then)
            Exception exception = assertThrows(Exception.class, () ->
                    mockMvc.perform(get("/api/auth/refresh")
                            .header(HttpHeaders.AUTHORIZATION, authHeader))
            );

            assertThat(exception.getCause()).isInstanceOf(IllegalAccessException.class);
            assertThat(exception.getCause().getMessage()).isEqualTo("INVALID BEARER TOKEN");
            verify(authService, times(1)).refreshToken(eq(authHeader));
        }
    }
}
