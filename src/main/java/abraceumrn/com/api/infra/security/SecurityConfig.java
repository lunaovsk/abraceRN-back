package abraceumrn.com.api.infra.security;


import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

/**
 * Configuração central de segurança da aplicação.
 *
 * Define:
 * - Política de sessão stateless (REST API com JWT)
 * - Regras de autorização por endpoint
 * - Codificação/decodificação de tokens JWT
 * - Estratégia de autenticação OAuth2 com JWT
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Cria um decoder JWT que valida tokens assinados com HS256.
     *
     * @return JwtDecoder configurado com a chave secreta
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    /**
     * Cria um encoder JWT para assinar novos tokens com HS256.
     *
     * @return JwtEncoder configurado com a chave secreta
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(secret.getBytes()));
    }

    /**
     * Define a cadeia de filtros de segurança com regras de autorização.
     *
     * Regras de autorização:
     * - /login (POST) - público
     * - /login/create (POST) - público
     * - Swagger docs - público
     * - /dashboard/admin/** - apenas ADMIN
     * - Demais endpoints - requer autenticação
     *
     * Configuração:
     * - CSRF desabilitado (API REST stateless)
     * - Session stateless (sem cookies de sessão, via JWT)
     * - Logout habilitado com redirect
     * - Remember-me configurado
     * - OAuth2 Resource Server com JWT
     *
     * @param httpSecurity builder de segurança
     * @return cadeia de filtros configurada
     * @throws Exception se houver erro na configuração
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        .requestMatchers("/dashboard/admin/**").hasAuthority("SCOPE_ROLE_ADMIN")
                        .anyRequest().authenticated())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())
                .rememberMe(rememberMe -> rememberMe.key("usuario-autenticado"))
                .oauth2ResourceServer(auth2 -> auth2.jwt(Customizer.withDefaults()));
        return httpSecurity.build();
    }

    /**
     * Encoder de passwords usando BCrypt com strength padrão (10).
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Gerenciador de autenticação padrão do Spring.
     *
     * Utilizado para processar tentativas de login.
     *
     * @param configuration configuração de autenticação
     * @return AuthenticationManager
     * @throws Exception se houver erro na configuração
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
