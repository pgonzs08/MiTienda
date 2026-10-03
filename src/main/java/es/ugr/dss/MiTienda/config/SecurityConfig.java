package es.ugr.dss.MiTienda.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	@Bean 
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/cart/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/products").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers(PathRequest.toH2Console()).permitAll()
				.anyRequest().authenticated()
				)
		.formLogin(form -> form
				.loginPage("/login").permitAll()
				.defaultSuccessUrl("/index", true)
				)
		.logout(logout -> logout
				.logoutUrl("/logout")
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
	/**
	 * USUARIOS DE PRUEBA, NO DEJAR EN LA APP EN DESPLIEGUE
	 * @param encoder
	 * @return
	 */
	@Bean
    UserDetailsService users(PasswordEncoder encoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password(encoder.encode("user123"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}