package newfarma.controller;

import newfarma.config.JwtAuthFilter;
import newfarma.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = HealthCheckController.class,
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
@DisplayName("Pruebas Unitarias - HealthCheckController")
class HealthCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("Operaciones de Verificación de Salud (Health Endpoints)")
    class HealthEndpointsTests {

        @Test
        @DisplayName("GET /api/check - Debe responder HTTP 200 con cuerpo 'OK'")
        void check_ShouldReturn200AndOkString() throws Exception {
            mockMvc.perform(get("/api/check"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("OK"));
        }

        @Test
        @DisplayName("GET /api/check/health - Debe responder HTTP 200 con cuerpo 'OK'")
        void health_ShouldReturn200AndOkString() throws Exception {
            mockMvc.perform(get("/api/check/health"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("OK"));
        }

        @Test
        @DisplayName("GET /api/check/healthcheck - Debe responder HTTP 200 con cuerpo 'OK'")
        void healthcheck_ShouldReturn200AndOkString() throws Exception {
            mockMvc.perform(get("/api/check/healthcheck"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("OK"));
        }

        @Test
        @DisplayName("GET /api/check/ - Debe responder HTTP 200 con mensaje de funcionamiento")
        void root_ShouldReturn200AndStatusMessage() throws Exception {
            mockMvc.perform(get("/api/check/"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("API funcionando correctamente"));
        }
    }

    @Nested
    @DisplayName("Validación de Métodos HTTP no permitidos")
    class MethodNotAllowedTests {

        @Test
        @DisplayName("POST /api/check - Debe rechazar con HTTP 405 Method Not Allowed")
        void check_WhenPostMethod_ShouldReturn405MethodNotAllowed() throws Exception {
            mockMvc.perform(post("/api/check"))
                    .andExpect(status().isMethodNotAllowed());
        }
    }
}
