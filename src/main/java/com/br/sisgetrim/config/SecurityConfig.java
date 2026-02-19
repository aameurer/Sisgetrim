package com.br.sisgetrim.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/login", "/cadastro", "/css/**", "/js/**", "/img/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .usernameParameter("documento") // Mapeia o campo CPF/CNPJ como username
                                                .defaultSuccessUrl("/dashboard", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll())
                                .rememberMe(rm -> rm.key("uniqueAndSecretSisgetrimKey"))
                                .sessionManagement(session -> session
                                                .maximumSessions(1)
                                                .sessionRegistry(sessionRegistry())
                                                .expiredUrl("/login?expired"));

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public org.springframework.security.core.session.SessionRegistry sessionRegistry() {
                return new org.springframework.security.core.session.SessionRegistryImpl();
        }

        @Bean
        public org.springframework.security.web.session.HttpSessionEventPublisher httpSessionEventPublisher() {
                return new org.springframework.security.web.session.HttpSessionEventPublisher();
        }
}
