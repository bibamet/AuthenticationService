package com.example.authenticationservice.filter;

import com.example.authenticationservice.dto.JwtAuthentication;
import com.example.authenticationservice.jwtprovider.JwtProvider;
import com.example.authenticationservice.service.AuthService;
import com.example.authenticationservice.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilterOncePerRequest extends OncePerRequestFilter {

        private static final String AUTHORIZATION = "Authorization";
        private final JwtProvider jwtProvider;
        private final AuthService authService;

        @Override
        public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain fc)
            throws IOException, ServletException {
            final String token = getTokenFromRequest((HttpServletRequest) request);
            if (token != null && authService.validateAccessToken(token)) {
                final Claims claims = jwtProvider.getAccessClaims(token);
                final JwtAuthentication jwtInfoToken = JwtUtils.generate(claims);
                jwtInfoToken.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
            }
            fc.doFilter(request, response);
        }

        private String getTokenFromRequest(HttpServletRequest request) {
            final String bearer = request.getHeader(AUTHORIZATION);
            if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
                return bearer.substring(7);
            }
            return null;
        }

}
