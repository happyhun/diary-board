package com.example.diaryboard.global.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        RequestMatcher permitAllMatcher = new OrRequestMatcher(
                new AntPathRequestMatcher("/api/v1/members", "POST"),
                new AntPathRequestMatcher("/api/v1/members/login", "POST"),
                new AntPathRequestMatcher("/api/v1/posts/**", "GET"),
                new AntPathRequestMatcher("/actuator/**", "GET"),
                new AntPathRequestMatcher("/h2-console/**")
        );

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(permitAllMatcher).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/v1/members/reissue")).hasAuthority("SCOPE_REFRESH")
                .anyRequest().hasAuthority("SCOPE_ACCESS")
        );

        http.oauth2ResourceServer(oauth2 -> oauth2
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 인증되지 않은 사용자가 보호된 리소스에 접근할 때 호출
                .accessDeniedHandler(new CustomAccessDeniedHandler()) // 인증된 사용자가 권한이 없는 리소스에 접근할 때 호출
                .jwt(jwt -> jwt.jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())) // JWT를 파싱하여 Authentication 객체로 변환
        );

        http.httpBasic(AbstractHttpConfigurer::disable); // Basic 인증 방식 사용 안 함 (JWT 사용)
        http.csrf(AbstractHttpConfigurer::disable); // CSRF 사용 안 함 (JWT 사용)
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션 사용 안 함 (JWT 사용)
        http.cors(cors -> cors.configurationSource(corsConfigurationSource())); // CORS 설정
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)); // 동일한 Origin에서만 프레임 허용

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // TODO: 프론트 서버 배포되면 origin 변경
        configuration.setAllowedMethods(List.of("GET", "POST", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 설정

        return source;
    }
}
