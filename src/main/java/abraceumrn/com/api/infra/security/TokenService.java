package abraceumrn.com.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

/**
 * Service responsável por gerar tokens JWT para autenticação.
 *
 * Cria tokens com informações do usuário autenticado e expira em 1 hora.
 */
@Service
public class TokenService {

    @Autowired
    private JwtEncoder encoder;

    /**
     * Calcula o tempo de expiração do token (1 hora a partir de agora).
     *
     * @return instante de expiração em timezone Brasil (-03:00)
     */
    public Instant expirationToken () {
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));
    }

    /**
     * Gera um token JWT com base nas informações do usuário autenticado.
     *
     * Token inclui:
     * - subject: nome do usuário
     * - scope: roles/permissions do usuário
     * - issuer: identificação da aplicação
     * - issued/expiration times: data de emissão e expiração
     *
     * @param authentication dados de autenticação do usuário
     * @return token JWT em formato string
     */
    public String generateToken(Authentication authentication) {
        String scopes = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        var claims = JwtClaimsSet.builder()
                .issuer("abreaceRN-back")
                .issuedAt(Instant.now())
                .expiresAt(expirationToken())
                .subject(authentication.getName())
                .claim("scope", scopes)
                .build();
        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}
