package es.ugr.dss.MiTienda.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	@Bean 
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/cart/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/catalog").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
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

}