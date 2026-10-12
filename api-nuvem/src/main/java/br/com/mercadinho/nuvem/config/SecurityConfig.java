package br.com.mercadinho.nuvem.configuracao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    // Esqueleto inicial. Exigir autenticação forte e HTTPS antes de publicar a API.
    @Bean
    SecurityFilterChain cadeiaDeSeguranca(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.requestMatchers("/api/saude", "/actuator/health").permitAll()
                .anyRequest().authenticated())
            .build();
    }
}
