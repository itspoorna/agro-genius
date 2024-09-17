package com.bvb.agroGenius.security;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.bvb.agroGenius.filter.JWTTokenValidatorFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity(debug = true) 
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class AgroGeniusSecurityConfig {

	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}
	
	@Bean
    public AuthenticationManager authenticationManager(
                                 AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

		// http.securityContext((context) -> context.requireExplicitSave(true));
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
					@Override
					public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
						CorsConfiguration config = new CorsConfiguration();
						config.setAllowedOrigins(Arrays.asList("http://localhost:5173/", "http://localhost:8081/"));
						config.setAllowedMethods(Collections.singletonList("*"));
						config.setAllowCredentials(true);
						config.setAllowedHeaders(Collections.singletonList("*"));
						config.setExposedHeaders(Collections.singletonList("*"));
						config.setMaxAge(3600L);
						return config;
					}
				}))
				.csrf(csrf -> csrf.disable())
				//.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
				//.addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
				.addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
				.authorizeHttpRequests(auth -> auth.requestMatchers("/contact","/validator","/user/signIn","/user/signUp","/user/signInWithGoogle", "/validator/verify","/products/getAll").permitAll().anyRequest().authenticated())
				.formLogin(Customizer.withDefaults()).httpBasic(Customizer.withDefaults())
				.exceptionHandling(exception -> exception.accessDeniedHandler(new RestAuthorizationEntryPoint()))
				.exceptionHandling(exception -> exception.authenticationEntryPoint(new RestAuthenticationEntryPoint()));
		return http.build();
	}
}
