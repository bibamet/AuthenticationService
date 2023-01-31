package com.example.authenticationservice.config;

//import com.example.authenticationservice.filter.JwtFilter;
import com.example.authenticationservice.filter.JwtFilterOncePerRequest;
import com.example.authenticationservice.jwtprovider.JwtProvider;
import com.example.authenticationservice.service.AuthService;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilterOncePerRequest jwtFilter;
//    private final FilterRegistrationBean jwtFilter;

    @SneakyThrows
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {

        return httpSecurity.httpBasic().disable()
                .csrf().disable()
//                .anonymous(AbstractHttpConfigurer::disable)
//                .exceptionHandling(AbstractHttpConfigurer::disable) // ExceptionTranslationFilter
//                .headers(AbstractHttpConfigurer::disable)           // HeaderWriterFilter
//                .logout(AbstractHttpConfigurer::disable)            // LogoutFilter
//                .requestCache(AbstractHttpConfigurer::disable)      // RequestCacheAwareFilter
//                .servletApi(AbstractHttpConfigurer::disable)        // SecurityContextHolderAwareRequestFilter
//                .securityContext(AbstractHttpConfigurer::disable)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(
                        authz -> authz.requestMatchers("/api/auth/login", "/api/auth/token", "/api/tokens", "/api/users", "/api/users/test/*").permitAll()
                                .anyRequest().authenticated()
                                .and()
                                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class))
                .build();

    }

}
