package es.ugr.dss.MiTienda.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import es.ugr.dss.MiTienda.model.User;
import es.ugr.dss.MiTienda.repository.UserRepo;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
	@Autowired
	private UserRepo userRepo;
	
	@Bean 
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
        // Allow APIs without CSRF
        .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))

        // Authorization rules
        .authorizeHttpRequests(auth -> auth

            // Allow Products REST API (ALL METHODS: GET, POST, DELETE…)
            .requestMatchers("/api/products/**").permitAll()
            
            // Admin-only REST API
            .requestMatchers("/api/users/**").hasRole("ADMIN")
            
            // Allow Register
            .requestMatchers("/register").permitAll()

            // Public static pages
            .requestMatchers("/", "/index", "/index.html",
                             "/css/**", "/js/**", "/images/**", "/webjars/**")
                .permitAll()

            // Public product browsing (MVC)
            .requestMatchers(HttpMethod.GET, "/products").permitAll()

            // Admin-only pages
            .requestMatchers("/admin/**",
                             "/products/add",
                             "/products/edit/**",
                             "/products/delete/**",
                             "/products/update/**")
                .hasRole("ADMIN")

            // Everything else requires login
            .anyRequest().authenticated()
        )

        // Browser login
        .formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/index", true)
            .permitAll()
        )

        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .permitAll()
        );

    return http.build();
	}
	
	@Bean
	PasswordEncoder password() {
		return new BCryptPasswordEncoder();
	}

	@Bean
    CommandLineRunner initAdminUser(PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepo.existsByUsername("admin")) {
                User newUser = new User();
                newUser.setUsername("admin");
                newUser.setPassword(passwordEncoder.encode("123&qwe&asD")); 
                newUser.setRole("ADMIN"); // Asignado rol ADMIN para acceder a /admin/**
                
                userRepo.save(newUser);
            }
        };
    }
	
}