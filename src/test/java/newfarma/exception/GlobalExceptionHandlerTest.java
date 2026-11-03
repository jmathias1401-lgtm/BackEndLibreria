package newfarma.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.lang.reflect.Method;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        when(request.getRequestURI()).thenReturn("/api/test-endpoint");
    }

    @Test
    @DisplayName("Debe manejar EntityNotFoundException y retornar HTTP 404 Not Found")
    void handleEntityNotFound_ShouldReturn404() {
        EntityNotFoundException ex = new EntityNotFoundException("Cliente no encontrado con ID 5");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleEntityNotFound(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getError()).isEqualTo("Not Found");
        assertThat(response.getBody().getMessage()).isEqualTo("Cliente no encontrado con ID 5");
        assertThat(response.getBody().getPath()).isEqualTo("/api/test-endpoint");
    }

    @Test
    @DisplayName("Debe manejar MethodArgumentNotValidException y retornar HTTP 400 Bad Request con detalles de validación")
    void handleValidationExceptions_ShouldReturn400WithErrors() throws NoSuchMethodException {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("productoDto", "nombre", "El nombre es obligatorio");
        FieldError fieldError2 = new FieldError("productoDto", "stock", "El stock no puede ser negativo");

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        Method method = this.getClass().getDeclaredMethod("setUp");
        MethodParameter parameter = new MethodParameter(method, -1);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationExceptions(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getError()).isEqualTo("Bad Request");
        assertThat(response.getBody().getValidationErrors()).containsEntry("nombre", "El nombre es obligatorio");
        assertThat(response.getBody().getValidationErrors()).containsEntry("stock", "El stock no puede ser negativo");
    }

    @Test
    @DisplayName("Debe manejar IllegalArgumentException y retornar HTTP 400 Bad Request")
    void handleIllegalArgument_ShouldReturn400() {
        IllegalArgumentException ex = new IllegalArgumentException("Parámetro inválido");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleIllegalArgument(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo("Parámetro inválido");
    }

    @Test
    @DisplayName("Debe manejar BadCredentialsException y retornar HTTP 401 Unauthorized")
    void handleBadCredentials_ShouldReturn401() {
        BadCredentialsException ex = new BadCredentialsException("Bad credentials");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBadCredentials(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(401);
        assertThat(response.getBody().getMessage()).contains("Credenciales inválidas");
    }

    @Test
    @DisplayName("Debe manejar AccessDeniedException y retornar HTTP 403 Forbidden")
    void handleAccessDenied_ShouldReturn403() {
        AccessDeniedException ex = new AccessDeniedException("Access denied");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAccessDenied(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(403);
        assertThat(response.getBody().getMessage()).contains("No cuenta con permisos suficientes");
    }

    @Test
    @DisplayName("Debe manejar MaxUploadSizeExceededException y retornar HTTP 413 Payload Too Large")
    void handleMaxUploadSize_ShouldReturn413() {
        MaxUploadSizeExceededException ex = new MaxUploadSizeExceededException(5000000);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleMaxUploadSize(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(413);
        assertThat(response.getBody().getMessage()).contains("tamaño máximo permitido");
    }

    @Test
    @DisplayName("Debe manejar Exception general no controlada y retornar HTTP 500 Internal Server Error")
    void handleGlobalException_ShouldReturn500() {
        RuntimeException ex = new RuntimeException("Error inesperado en base de datos");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGlobalException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(500);
        assertThat(response.getBody().getMessage()).contains("error interno en el servidor");
    }
}

