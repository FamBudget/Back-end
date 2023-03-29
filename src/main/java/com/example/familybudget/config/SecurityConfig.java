package com.example.familybudget.config;

import com.example.familybudget.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final RestAccessDeniedHandler accessDeniedHandler;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    private static final String SWAGGER = "/swagger-ui**/**";
    private static final String USER_ENDPOINT = "/users/**";
    private static final String ADMIN_ENDPOINT = "/admins/**";
    private static final String REGISTRATION_ENDPOINT = "/registration";
    private static final String AUTHENTICATION_ENDPOINT = "/authentication";
    private static final String ACTIVATE_ENDPOINT = "/activate/*";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(SWAGGER).permitAll()
                .antMatchers("/v*/api-docs",
                        "/swagger-resources",
                        "/swagger-resources/configuration/ui",
                        "/swagger-resources/configuration/security").permitAll()
                .antMatchers(REGISTRATION_ENDPOINT).permitAll()
                .antMatchers(ACTIVATE_ENDPOINT).permitAll()
                .antMatchers(AUTHENTICATION_ENDPOINT).permitAll()
                .antMatchers(USER_ENDPOINT).hasRole("USER")
                .antMatchers(ADMIN_ENDPOINT).hasRole("ADMIN")
                .anyRequest().authenticated().and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
