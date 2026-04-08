package campus_nexus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityConfig handles authentication and authorization.
 * Currently configured to permit all requests for smooth development and testing.
 * Future integration: OAuth2 (Google Login) and Role-based Access Control.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for API testing with Postman
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Permit all requests during initial development
                );

        // OAuth2 login configuration will be defined here later
        // .oauth2Login(Customizer.withDefaults());

        return http.build();
    }
}