package main.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig
{
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        // Generally require basic authentication, but allow requests to see activities and creation of users.
        // Admin role also required to delete activities or users.
        http.authorizeHttpRequests((authorize) -> authorize.requestMatchers(HttpMethod.GET, "/activities/**").permitAll()
                                                           .requestMatchers(HttpMethod.DELETE, "/activities/**").hasRole("ADMIN")
                                                           .requestMatchers(HttpMethod.POST, "/users/**").permitAll()
                                                           .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                                                           .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService()
    {
        return new CustomUserDetailsService();
    }

    @Bean
    PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

}
