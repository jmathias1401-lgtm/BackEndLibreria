package newfarma.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import newfarma.model.Cargo;
import newfarma.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - JwtService")
class JwtServiceTest {

    private JwtService jwtService;
    private static final String TEST_SECRET = "dGVzdFNlY3JldEtleUZvckp3dFRlc3RpbmdQdXJwb3Nlcw==";
    private static final long ACCESS_EXPIRATION  = 900_000L;
    private static final long REFRESH_EXPIRATION = 86_400_000L;
    private Usuario sampleUser;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey",        TEST_SECRET);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration",    ACCESS_EXPIRATION);
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", REFRESH_EXPIRATION);
        Cargo cargo = new Cargo();
        cargo.setIdcargo(2L);
        cargo.setNombrecargo("FARMACEUTICO");
        sampleUser = Usuario.builder()
                .idusuario(10L)
                .nombreusuario("maria.garcia")
                .clave("encodedPass")
                .fechacreacion(new Date())
                .cargo(cargo)
                .build();
    }

    private String buildRawToken(String subject, long expirationMs) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(TEST_SECRET));
        return Jwts.builder()
                .setId("10")
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    @Nested
    @DisplayName("Operacion: generateToken(Usuario)")
    class GenerateTokenTests {
        @Test
        @DisplayName("Debe generar un token no nulo")
        void generateToken_ShouldReturnNonNullToken() {
            String token = jwtService.generateToken(sampleUser);
            assertThat(token).isNotNull().isNotBlank();
        }
        @Test
        @DisplayName("El subject del token debe ser el nombreusuario")
        void generateToken_SubjectShouldMatchUsername() {
            String token = jwtService.generateToken(sampleUser);
            assertThat(jwtService.extractUsername(token)).isEqualTo("maria.garcia");
        }
    }

    @Nested
    @DisplayName("Operacion: generateRefreshToken(Usuario)")
    class GenerateRefreshTokenTests {
        @Test
        @DisplayName("El refresh token debe tener el mismo subject que el usuario")
        void generateRefreshToken_ShouldHaveSameSubject() {
            String refreshToken = jwtService.generateRefreshToken(sampleUser);
            assertThat(refreshToken).isNotNull().isNotBlank();
            assertThat(jwtService.extractUsername(refreshToken)).isEqualTo("maria.garcia");
        }
    }

    @Nested
    @DisplayName("Operacion: extractUsername(String token)")
    class ExtractUsernameTests {
        @Test
        @DisplayName("Debe extraer el username de un token valido")
        void extractUsername_WhenValidToken_ShouldReturnUsername() {
            String token = buildRawToken("maria.garcia", ACCESS_EXPIRATION);
            assertThat(jwtService.extractUsername(token)).isEqualTo("maria.garcia");
        }
        @Test
        @DisplayName("Debe lanzar excepcion con token malformado")
        void extractUsername_WhenMalformedToken_ShouldThrow() {
            assertThrows(Exception.class, () -> jwtService.extractUsername("no.es.un.jwt"));
        }
    }

    @Nested
    @DisplayName("Operacion: isTokenValid(token, userDetails)")
    class IsTokenValidTests {
        @Test
        @DisplayName("Debe retornar true para token valido con username coincidente")
        void isTokenValid_WhenValid_ShouldReturnTrue() {
            String token = jwtService.generateToken(sampleUser);
            assertThat(jwtService.isTokenValid(token, sampleUser)).isTrue();
        }
        @Test
        @DisplayName("Debe retornar false cuando el username no coincide")
        void isTokenValid_WhenUsernameMismatch_ShouldReturnFalse() {
            String token = jwtService.generateToken(sampleUser);
            Usuario otro = Usuario.builder()
                    .idusuario(99L).nombreusuario("otro.usuario").clave("pass")
                    .fechacreacion(new Date()).cargo(sampleUser.getCargo()).build();
            assertThat(jwtService.isTokenValid(token, otro)).isFalse();
        }
        @Test
        @DisplayName("Debe lanzar excepcion con token expirado")
        void isTokenValid_WhenExpired_ShouldThrow() {
            String expired = buildRawToken("maria.garcia", -1000L);
            assertThrows(Exception.class, () -> jwtService.isTokenValid(expired, sampleUser));
        }
    }
}
