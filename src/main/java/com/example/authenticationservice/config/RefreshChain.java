//package com.example.authenticationservice.config;
//
//import com.example.authenticationservice.filter.JwtFilter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.autoconfigure.security.SecurityProperties;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.SecurityBuilder;
//import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@Order(SecurityProperties.BASIC_AUTH_ORDER - 10)
//@RequiredArgsConstructor
//@EnableWebSecurity
//public class RefreshChain extends SecurityConfigurerAdapter {
//
//    private final JwtFilter jwtFilter;
//
//    @Override
//    public void init(SecurityBuilder builder) throws Exception {
//
//    }
//
//    @Override
//    public void configure(SecurityBuilder builder) throws Exception {
//        HttpSecurity build = (HttpSecurity) builder.build();
//        build.authorizeHttpRequests((auth) -> auth.requestMatchers("/api/auth/refresh").hasAnyAuthority("USER", "ADMIN").and().addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class));
//    }
//}
