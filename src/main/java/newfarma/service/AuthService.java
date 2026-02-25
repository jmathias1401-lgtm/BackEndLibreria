package newfarma.service;

import lombok.RequiredArgsConstructor;
import newfarma.auth.dto.LoginRequest;
import newfarma.auth.dto.TokenResponse;
import newfarma.model.Token;
import newfarma.model.Usuario;
import newfarma.repository.TokenRepository;
import newfarma.usuario.UsuarioRepositoryJPA;
import newfarma.usuario.UsuarioService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioService usuarioService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepositoryJPA usuarioRepositoryJPA;
    public TokenResponse register(Usuario usuario)
    {
        var user=new Usuario();
        if (usuario.getIdusuario()!=null){
            user = Usuario.builder()
                    .idusuario(usuario.getIdusuario())
                    .nombreusuario(usuario.getNombreusuario())
                    .clave(passwordEncoder.encode(usuario.getPassword()))
                    .fechacreacion(usuario.getFechacreacion())
                    .cargo(usuario.getCargo())
                    .build();
        }
        else {
            user = Usuario.builder()
                    .nombreusuario(usuario.getNombreusuario())
                    .clave(passwordEncoder.encode(usuario.getPassword()))
                    .fechacreacion(usuario.getFechacreacion())
                    .cargo(usuario.getCargo())
                    .build();
        }

        var savedUser = usuarioService.save(user);
        if (savedUser == null) {
            throw new RuntimeException("User already exists");
        }
        var jwtToken = jwtService.generateToken(savedUser);
        var refreshToken = jwtService.generateRefreshToken(savedUser);
        SaveUserToken(savedUser, jwtToken);
        return new TokenResponse(jwtToken, refreshToken);
    }

    public TokenResponse loggin(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.usuario(),
                        loginRequest.password()
                )
        );
        var user = usuarioRepositoryJPA.findByNombreusuario(loginRequest.usuario())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + loginRequest.usuario()));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revoqueAllUserToken(user);
        SaveUserToken(user, jwtToken);
        return new TokenResponse(jwtToken, refreshToken);
    }

    private void SaveUserToken(Usuario user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenTipe(Token.tokentipe.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revoqueAllUserToken(final Usuario user) {
        final List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getIdusuario());
        if (!validUserTokens.isEmpty()) {
            for (final Token token : validUserTokens) {
                token.setExpired(true);
                token.setRevoked(true);
            }
            tokenRepository.saveAll(validUserTokens);
        }
    }

    public TokenResponse refreshToken(final String authHeader) throws IllegalAccessException {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalAccessException("INVALID BEARER TOKEN");
        }
        final String refreshToken = authHeader.substring(7);
        final String usuario = jwtService.extractUsername(refreshToken);
        if (usuario == null) {
            throw new IllegalAccessException("Invalid Refresh Token");
        }
        final Usuario user = usuarioRepositoryJPA.findByNombreusuario(usuario)
                .orElseThrow(() -> new UsernameNotFoundException(usuario));
        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new IllegalAccessException("Invalid Refresh Token");
        }
        final String accessToken = jwtService.generateToken(user);
        revoqueAllUserToken(user);
        SaveUserToken(user, accessToken);
        return new TokenResponse(accessToken, refreshToken);
    }
}
