package com.api.alquilaya.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.api.alquilaya.repositories.IUsuariosRepository;

@Configuration
public class SecurityConfig {
    
    @Bean 
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(IUsuariosRepository usuariosRepository) {
        return email -> usuariosRepository.findByEmail(email)
            .map(usuario -> User.withUsername(usuario.getEmail())
                .password(usuario.getPassword())
                .roles(usuario.getRol())
                .disabled(!usuario.isActivo())
                .build())
            .orElseThrow(() ->
                    new UsernameNotFoundException("Usuario no encontrado"));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/**").hasAnyRole("ADMIN", "OPERADOR")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .successHandler((request, response, authentication) -> {
                    boolean esAdmin = authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                    String destino = esAdmin ? "/api/admin/prueba" : "/api/operador/prueba";
                    response.sendRedirect(destino);
                })
                .permitAll()
            )
            .httpBasic(Customizer.withDefaults())
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    } 
}
