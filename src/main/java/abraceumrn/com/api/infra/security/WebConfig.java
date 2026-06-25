package abraceumrn.com.api.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuração CORS (Cross-Origin Resource Sharing) da aplicação.
 *
 * Permite requisições de clientes específicos (frontend em desenvolvimento).
 * Para produção, restringir as origins a apenas domínios confiáveis.
 */
@Configuration
public class WebConfig {

    /**
     * Configura as regras de CORS para todos os endpoints.
     *
     * Permite:
     * - Origins: 127.0.0.1/localhost:5500 (Live Server), localhost:3000 (React dev)
     * - Métodos: GET, POST, PUT, DELETE, OPTIONS
     * - Headers: Authorization, Content-Type
     * - Credentials: cookies/auth headers
     *
     * @return fonte de configuração CORS usada pelo Spring Security
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://127.0.0.1:5500",
                "http://localhost:5500",
                "http://localhost:3000",
                "http://127.0.0.1:3000"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
