package com.prodevans.QRBaseLogin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AppSecurity {
    // "/ws-login/**"
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf->csrf.disable()).authorizeHttpRequests(auth->auth.requestMatchers("/receiver").permitAll()
                .requestMatchers("/h2-console/**", "/css/**", "/js/**",
                "/img/**", "/websocket-server","/api/login").permitAll()
                .requestMatchers("/test").permitAll()
                .requestMatchers("/resources/**").permitAll()
                        .requestMatchers("/success").authenticated()
                .requestMatchers(HttpMethod.POST).permitAll().anyRequest().permitAll())
                .formLogin(login->login.loginPage("/").defaultSuccessUrl("/success"));
        return httpSecurity.build();

    }
}
