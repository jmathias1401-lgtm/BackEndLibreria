package newfarma.repository;

import newfarma.model.Token;
import newfarma.model.Usuario;
import newfarma.usuario.UsuarioRepositoryJPA;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("Pruebas de Repositorio - TokenRepository")
class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UsuarioRepositoryJPA usuarioRepositoryJPA;

    @Test
    @DisplayName("findByToken retorna token existente")
    void findByToken_ShouldReturnToken() {
        Usuario user = new Usuario();
        user.setNombreusuario("tokenuser_" + System.currentTimeMillis());
        user.setClave("pass123");
        user = usuarioRepositoryJPA.save(user);

        String tokenStr = "jwt_token_" + System.currentTimeMillis();
        Token token = Token.builder()
                .token(tokenStr)
                .tokenTipe(Token.tokentipe.BEARER)
                .revoked(false)
                .expired(false)
                .user(user)
                .build();
        tokenRepository.save(token);

        Optional<Token> result = tokenRepository.findByToken(tokenStr);

        assertThat(result).isPresent();
        assertThat(result.get().getToken()).isEqualTo(tokenStr);
        assertThat(result.get().getUser().getIdusuario()).isEqualTo(user.getIdusuario());
    }

    @Test
    @DisplayName("findAllValidTokenByUser retorna tokens no revocados o no expirados del usuario")
    void findAllValidTokenByUser_ShouldReturnValidTokens() {
        Usuario user = new Usuario();
        user.setNombreusuario("validuser_" + System.currentTimeMillis());
        user.setClave("pass123");
        user = usuarioRepositoryJPA.save(user);

        Token validToken = Token.builder()
                .token("valid_token_" + System.currentTimeMillis())
                .tokenTipe(Token.tokentipe.BEARER)
                .revoked(false)
                .expired(false)
                .user(user)
                .build();

        Token expiredAndRevokedToken = Token.builder()
                .token("expired_token_" + System.currentTimeMillis())
                .tokenTipe(Token.tokentipe.BEARER)
                .revoked(true)
                .expired(true)
                .user(user)
                .build();

        tokenRepository.save(validToken);
        tokenRepository.save(expiredAndRevokedToken);

        List<Token> validTokens = tokenRepository.findAllValidTokenByUser(user.getIdusuario());

        assertThat(validTokens).hasSize(1);
        assertThat(validTokens.get(0).getToken()).isEqualTo(validToken.getToken());
    }
}
