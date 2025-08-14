package org.zeta.resturant.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/menu").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/menu/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/menu/*").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/menu").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/bookings").hasAnyRole("MANAGER", "WAITER")
                        .requestMatchers(HttpMethod.POST,"/api/bookings/book").hasAnyRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET,"/api/orders/live").hasAnyRole("KITCHEN","WAITER","MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/orders/*/markPrepared").hasRole("KITCHEN")
                        .requestMatchers(HttpMethod.GET,"/api/orders").permitAll()
                        .requestMatchers("/api/orders/place").hasAnyRole("WAITER", "MANAGER", "KITCHEN")
                        .requestMatchers("/api/bills/**").hasRole("MANAGER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

