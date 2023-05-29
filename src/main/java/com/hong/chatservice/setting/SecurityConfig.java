package com.hong.chatservice.setting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hong.chatservice.member.application.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final MemberDetailsService memberDetailsService;

    private final JwtProvider jwtProvider;
    private final JsonAuthenticationSuccessHandler jsonAuthenticationSuccessHandler;
    private final JsonAuthenticationFailureHandler jsonAuthenticationFailureHandler;
    private final ObjectMapper objectMapper;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable();

        http.authorizeHttpRequests()
                .requestMatchers("/login").permitAll()
                .requestMatchers(HttpMethod.OPTIONS,"/**/*").permitAll()
                .requestMatchers(HttpMethod.GET,"/rooms").permitAll()
                .anyRequest().authenticated();

        http
                .cors();

        http
                .addFilterBefore(jsonLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

        /*http
                .exceptionHandling()
                .accessDeniedHandler(ajaxAccessDeniedHandler());*/

        return http.build();
    }

    @Bean
    public AccessDeniedHandler ajaxAccessDeniedHandler() {
        return new JsonAccessDeniedHandler();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(memberDetailsService);

        return new ProviderManager(provider);
    }

    @Bean
    public JsonLoginProcessingFilter jsonLoginProcessingFilter() {
        JsonLoginProcessingFilter jsonLoginProcessingFilter = new JsonLoginProcessingFilter(objectMapper);
        jsonLoginProcessingFilter.setAuthenticationManager(authenticationManager());
        jsonLoginProcessingFilter.setAuthenticationSuccessHandler(jsonAuthenticationSuccessHandler);
        jsonLoginProcessingFilter.setAuthenticationFailureHandler(jsonAuthenticationFailureHandler);
        return jsonLoginProcessingFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:5500/");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowCredentials(true);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}