package abraceumrn.com.api.infra.security;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Dashboard Abrace RN")
                        .version("1.0")
                        .description("Documentação da API de gestão de estoque desenvolvida para a ONG que auxilia mulheres em situação de vulnerabilidade no pós-parto, garantindo a distribuição organizada de itens essenciais para recém-nascidos.")
                );
    }
}

