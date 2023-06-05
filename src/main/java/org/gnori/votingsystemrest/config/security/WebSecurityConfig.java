package org.gnori.votingsystemrest.config.security;

import lombok.RequiredArgsConstructor;
import org.gnori.votingsystemrest.model.entity.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final AuthenticationProvider authProvider;
  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationEntryPoint authenticationEntryPoint;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .csrf().disable()
        .authorizeHttpRequests(
            requests -> requests
                .requestMatchers("/api/v1/auth", "/api/v1/users").permitAll()
                .requestMatchers(
                    "/v2/api-docs", "/v3/api-docs/**", "/swagger-resources",
                    "/swagger-resources/**", "/configuration/ui", "/configuration/security",
                    "/swagger-ui/**", "/webjars/**", "/swagger-ui.html"
                ).permitAll()
                .requestMatchers("/api/v1/admin/**").hasAuthority(Role.ADMIN.name())
                .requestMatchers("/api/v1/users/**").hasAuthority(Role.USER.name())
                .anyRequest().authenticated()
    )
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authenticationProvider(authProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint);

    return http.build();

  }

}
