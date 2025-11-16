package com.ranker.web.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    // https://docs.spring.io/spring-security/reference/migration-7/configuration.html
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                            "/login",
                            "/register",
                            "/lists",
                            "/register/**",
                            "/css/**",
                            "/js/**"
                    ).permitAll()
                    .anyRequest().authenticated()
            )

            .formLogin(form -> form
                    .loginPage("/login")
                    .defaultSuccessUrl("/lists", true)
                    .loginProcessingUrl("/login")
                    .failureUrl("/login?error=true")
                    .permitAll()
            )

            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .permitAll()
            );

        return http.build();
    }
}
