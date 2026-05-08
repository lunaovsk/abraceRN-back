package abraceumrn.com.api.infra.security;


import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

//Verifica se a rota exige alguma autenticação/role.
@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Value("${api.security.token.secret}")
    private String secret;

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        // Converte a string do secret para bytes e cria o encoder usando a biblioteca Nimbus
        return new NimbusJwtEncoder(new ImmutableSecret<>(secret.getBytes()));
    }

    //Configuração de acesso com base em exemplos do package org.springframework.security.config.annotation.web.builders;
    //Utilizei o metodo com admin na url para que eu possa acessar qualquer rota com user sem definir as roles mas as ADMIN serem bloqueadas.
    //Security filter permite qual rota deve ou não ser acessada.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) //desativa o forms padrao
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //Configura quantos usuarios estão ativos, duração de sessão e afins.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()//Permite qualquer role em /login acessar o endereço.
                        .requestMatchers(HttpMethod.POST, "/login/create").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        .requestMatchers("/dashboard/admin/**").hasAuthority("SCOPE_ROLE_ADMIN")
                        .anyRequest().authenticated())
                .oauth2ResourceServer( auth2 -> auth2.jwt(Customizer.withDefaults()));
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}
