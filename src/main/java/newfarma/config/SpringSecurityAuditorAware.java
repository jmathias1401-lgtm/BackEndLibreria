package newfarma.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Proveedor del usuario auditor actual para Spring Data JPA.
 * Extrae el nombre de usuario desde el contexto de Spring Security (JWT).
 * Si la operación ocurre sin usuario autenticado (ej: batch, seeds o pruebas), asigna "SYSTEM".
 */
@Component("auditorProvider")
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null 
                || !authentication.isAuthenticated() 
                || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.of("SYSTEM");
        }

        return Optional.ofNullable(authentication.getName());
    }
}

