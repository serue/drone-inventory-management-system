package com.nyasha.drone.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)  // Disable CORS if not required
                .csrf(csrf -> csrf  // CSRF is enabled by default; you can customize it here if needed
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())  // Example: storing the token in a cookie
                )  // Enable CSRF for session-based authentication
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/drone/auth/**").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")  
                        // Permit access to authentication endpoints
                        .anyRequest().authenticated()  // Protect all other endpoints
                )
                .formLogin(form -> form
                        .loginPage("/drone/auth/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/drone/auth/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/drone/auth/logout")  // Custom logout URL
                        .logoutSuccessUrl("/drone/auth/login")  // Redirect to login page after logout
                        .invalidateHttpSession(true)  // Invalidate session on logout
                        .clearAuthentication(true)  // Clear authentication on logout
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // Use session-based authentication
                )
                .authenticationProvider(authenticationProvider);  // Use your existing AuthenticationProvider

        return http.build();
    }
}
