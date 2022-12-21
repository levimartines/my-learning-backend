package com.levimartines.mylearningbackend.config;

import com.levimartines.mylearningbackend.security.AuthenticationService;
import com.levimartines.mylearningbackend.security.JWTAuthenticationFilter;
import com.levimartines.mylearningbackend.security.JWTAuthorizationFilter;
import com.levimartines.mylearningbackend.security.JWTUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;
    private final AuthenticationService authenticationService;
    private final String frontendUrl;

    private static final String[] PUBLIC_MATCHERS_SWAGGER = {
        "/swagger-ui/**",
        "/v3/api-docs/**"
    };
    private static final String[] PUBLIC_MATCHERS_POST = {
        "/users"
    };
    private static final String[] PUBLIC_MATCHERS_PUT = {
        "/users/confirm-registration"
    };

    public SecurityConfig(
        @Qualifier("customUserDetailsService") UserDetailsService userDetailsService, JWTUtil jwtUtil,
        @Value("${service.frontend-host}") String frontendUrl, AuthenticationService authenticationService) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.frontendUrl = frontendUrl;
        this.authenticationService = authenticationService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers()
            .xssProtection()
            .and()
            .contentSecurityPolicy("script-src 'self'");
        http.cors(Customizer.withDefaults());
        http.csrf().disable();
        http.authorizeHttpRequests(authz -> authz
            .requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
            .requestMatchers(HttpMethod.PUT, PUBLIC_MATCHERS_PUT).permitAll()
            .requestMatchers(PUBLIC_MATCHERS_SWAGGER).permitAll()
            .anyRequest().authenticated());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));
        http.addFilter(new JWTAuthenticationFilter(authenticationManager, jwtUtil, authenticationService));
        http.addFilter(new JWTAuthorizationFilter(authenticationManager, jwtUtil, userDetailsService));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontendUrl));
        configuration.setAllowedMethods(List.of("POST", "GET", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
