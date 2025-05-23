package com.example.pdponline.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true
)
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${security.whitelist}")
    private String[] whiteList;

    private final AuthEntryPoint authEntryPoint;
    private final MyAccessDeniedHandler accessDeniedHandler;
    private final AuthenticationProvider jwtProvider;
    private final JwtFilter jwtFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> corsConfigurationSource())
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(whiteList)
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(jwtProvider)
                .exceptionHandling(
                        httpExceptionHandler -> {
                            httpExceptionHandler.accessDeniedHandler(accessDeniedHandler);
                            httpExceptionHandler.authenticationEntryPoint(authEntryPoint);
                        }
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow credentials (cookies, authentication)
        configuration.setAllowCredentials(true);

        // Specify allowed origins. You can set specific origins or allow all (not recommended in production)
        configuration.setAllowedOriginPatterns(List.of("*"));

        // Allow all headers
        configuration.setAllowedHeaders(List.of("*"));

        // Allow HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allow specific headers like Authorization
        configuration.setExposedHeaders(List.of("Authorization"));

        // Apply to all paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
