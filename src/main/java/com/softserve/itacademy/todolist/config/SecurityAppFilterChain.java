package com.softserve.itacademy.todolist.config;

import com.softserve.itacademy.todolist.config.auth.AuthEntryPointJwt;
import com.softserve.itacademy.todolist.config.auth.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityAppFilterChain {

    private final AuthEntryPointJwt authEntryPointJwt;
    private final AuthTokenFilter authTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPointJwt))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/").permitAll()
                        .requestMatchers("/api/users/**").hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().authenticated())

                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}