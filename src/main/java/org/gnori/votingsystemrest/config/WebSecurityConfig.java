package org.gnori.votingsystemrest.config;

import lombok.RequiredArgsConstructor;
import org.gnori.votingsystemrest.model.entity.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final AuthenticationProvider authProvider;

  private final AuthenticationEntryPoint authEntryPoint;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http
        .authorizeHttpRequests(
            requests -> requests
                .requestMatchers("/api/v1/admin/**").hasRole(Role.ADMIN.name())
                .requestMatchers("/api/v1/users/**").hasRole(Role.USER.name())
                .requestMatchers("/api/v1/auth/**").permitAll()
                .anyRequest().authenticated()
    )
        .authenticationProvider(authProvider)
        .exceptionHandling()
        .authenticationEntryPoint(authEntryPoint);

    return http.build();

  }


}
