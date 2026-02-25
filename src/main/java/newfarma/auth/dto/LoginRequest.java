package newfarma.auth.dto;

public record LoginRequest(
        String usuario,
        String password
) {
}
