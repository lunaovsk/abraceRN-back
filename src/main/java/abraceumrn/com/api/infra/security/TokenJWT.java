package abraceumrn.com.api.infra.security;

/**
 * DTO para resposta de autenticação contendo um token JWT.
 *
 * Este token deve ser incluído no header Authorization de requisições subsequentes:
 * {@code Authorization: Bearer <tokenJWT>}
 *
 * O token é válido por 1 hora.
 */
public record TokenJWT(
        String tokenJWT
) {
}
