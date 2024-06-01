package com.yangjae.lupine.config.security;

import com.yangjae.lupine.config.security.admin.AdminAuthenticationFailureHandler;
import com.yangjae.lupine.config.security.admin.AdminAuthenticationProvider;
import com.yangjae.lupine.config.security.admin.AdminAuthenticationSuccessHandler;
import com.yangjae.lupine.config.security.admin.CustomAdminUserDetailsService;
import com.yangjae.lupine.admin.service.AdminService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private static final String[] PERMIT_ALL = {
			"/", "/admin/login", "/resources/**", "/admin/assets/**"
	};

	private final CustomAdminUserDetailsService customAdminUserDetailsService;
	private final AdminService adminService;
	private final CustomAuthenticationEntryPoint authenticationEntryPoint;
	private final CustomAccessDeniedHandler accessDeniedHandler;

	public SecurityConfig(CustomAdminUserDetailsService customAdminUserDetailsService,
                          AdminService adminService, CustomAuthenticationEntryPoint authenticationEntryPoint, CustomAccessDeniedHandler accessDeniedHandler) {
		this.customAdminUserDetailsService = customAdminUserDetailsService;
		this.adminService = adminService;
		this.authenticationEntryPoint = authenticationEntryPoint;
		this.accessDeniedHandler = accessDeniedHandler;
    }

	@Configuration
	@Order(1)
	public static class AdminConfigurationAdapter {

		private final SecurityConfig securityConfig;

        public AdminConfigurationAdapter(SecurityConfig securityConfig) {
            this.securityConfig = securityConfig;
        }

		@Bean
		public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
			http
					.antMatcher("/admin/**")
					.authorizeRequests(authRequest ->
							authRequest
									.antMatchers(PERMIT_ALL).permitAll()
									.antMatchers("/admin/**").access("@customAdminUserDetailsService.hasType(authentication, 'ADMIN')")
									.anyRequest().denyAll())
					.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
							.authenticationEntryPoint(securityConfig.authenticationEntryPoint)
							.accessDeniedHandler(securityConfig.accessDeniedHandler))
					.csrf(AbstractHttpConfigurer::disable)
					.formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
							.usernameParameter("username")
							.passwordParameter("password")
							.loginProcessingUrl("/admin/login")
							.successHandler(securityConfig.adminAuthenticationSuccessHandler())
							.failureHandler(securityConfig.adminAuthenticationFailureHandler())
							.and().authenticationManager(new ProviderManager(securityConfig.adminAuthenticationProvider())))
					.logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
							.logoutUrl("/admin/logout")
							.logoutSuccessUrl("/admin/login")
							.invalidateHttpSession(true))
					.sessionManagement()
							.maximumSessions(1)
							.maxSessionsPreventsLogin(false)
			;

			return http.build();
		}
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Admin Provider
	@Bean
	public AdminAuthenticationProvider adminAuthenticationProvider() {
		return new AdminAuthenticationProvider(customAdminUserDetailsService, passwordEncoder());
	}

	// Admin Success Handler
	@Bean
	public AdminAuthenticationSuccessHandler adminAuthenticationSuccessHandler() {
		return new AdminAuthenticationSuccessHandler(adminService);
	}

	// Admin Failure Handler
	@Bean
	public AdminAuthenticationFailureHandler adminAuthenticationFailureHandler() {
		return new AdminAuthenticationFailureHandler(adminService);
	}

}
