package com.aizs.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable() // Disable CSRF for stateless applications
                .authorizeRequests()

                .antMatchers(
                        "/api/login",
                        "/api/register",
                        "/api/user/info",
                        "/api/user/update",
                        "/api/feedback/user",
                        "/api/feedback/submit",
                        "/api/feedback/delete",
                        "/api/feedback/delete/**",
                        "/api/feedback/all",
                        "/api/feedback/user/**",
                        "/api/feedback/export",
                        "/api/exam/records","/api/exam/records/export","/api/exam/records/{workerid}","/api/exam/records/{workerid}/export")
                .permitAll()
                // 其他接口需要认证
                .anyRequest().authenticated();
    }@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:8080");  // Adjust for production needs
        configuration.addAllowedOrigin("http://127.0.0.1:8080");
        configuration.setAllowCredentials(true);
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("PATCH");
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
