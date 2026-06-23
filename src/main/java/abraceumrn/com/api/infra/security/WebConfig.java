package abraceumrn.com.api.infra.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração CORS (Cross-Origin Resource Sharing) da aplicação.
 *
 * Permite requisições de clientes específicos (frontend em desenvolvimento).
 * Para produção, restringir as origins a apenas domínios confiáveis.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configura as regras de CORS para todos os endpoints.
     *
     * Permite:
     * - Origins: localhost:5500 (Live Server), localhost:3000 (React dev)
     * - Métodos: GET, POST, PUT, DELETE, OPTIONS
     * - Headers: todos
     * - Credentials: cookies/auth headers
     *
     * @param registry registry de CORS
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://127.0.0.1:5500", "http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}