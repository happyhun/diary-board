package com.example.diaryboard.config;


import com.example.diaryboard.config.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Value("${demo.secretKey}")
    private String secretKey;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        MacAlgorithm algorithm = MacAlgorithm.HS256;

        return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(secretKey.getBytes(), algorithm.getName()))
                .macAlgorithm(algorithm)
                .build();
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider(secretKey);
    }

    @Bean
    public SecurityFilterChain jwtChain(HttpSecurity http) throws Exception {
        http.
                authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/auth/**")
                                .hasAuthority("SCOPE_acc")
                                .anyRequest()
                                .permitAll()
                )
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer
                                .accessDeniedHandler(new OAuth2AccessDeniedHandler())
                                .authenticationEntryPoint(new OAuth2AuthenticationEntryPoint())
                                .jwt(jwt -> new JwtAuthenticationConverter())
                );

        return http.build();
    }
}
