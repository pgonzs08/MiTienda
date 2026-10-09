package es.ugr.dss.MiTienda.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/").permitAll()
				.requestMatchers("/catalog").permitAll()
				.requestMatchers("/cart").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/api/**").denyAll()
				.requestMatchers("/register").permitAll()
				.requestMatchers(PathRequest.toH2Console()).permitAll()
				.anyRequest().authenticated()
				)
		.formLogin(form -> form
				.loginPage("/login").permitAll()
				.defaultSuccessUrl("/index", true)
				)
		.logout(logout -> logout
				.logoutUrl("/logout")
				.invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
				.logoutSuccessUrl("/login?logout")
				)
		.csrf(csrf -> csrf
				.ignoringRequestMatchers(PathRequest.toH2Console())
				)
		.headers(headers -> headers
				.frameOptions(frame -> frame.sameOrigin())
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