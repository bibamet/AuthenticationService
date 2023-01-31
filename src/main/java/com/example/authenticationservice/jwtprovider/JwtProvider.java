package com.example.authenticationservice.jwtprovider;

import com.example.authenticationservice.dto.UserDto;
import com.example.authenticationservice.mapper.RoleMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtProvider {

    private final RoleMapper roleMapper;

    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;
    private final PrivateKey privateAccessKey;
    private final PrivateKey privateRefreshKey;
    private final PublicKey publicAccessKey;
    private final PublicKey publicRefreshKey;
    private final Integer expirationAccess;


    public JwtProvider(@Value("${internal.jwt-access-secret}") String jwtAccessSecret,
                       @Value("${internal.jwt-refresh-secret}") String jwtRefreshSecret,
                       PrivateKey privateAccessKey,
                       PrivateKey privateRefreshKey,
                       PublicKey publicRefreshKey,
                       PublicKey publicAccessKey,
                       RoleMapper roleMapper,
                       @Value("${internal.expiration-access-token}") Integer expirationAccess) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
        this.privateAccessKey = privateAccessKey;
        this.publicAccessKey = publicAccessKey;
        this.publicRefreshKey = publicRefreshKey;
        this.privateRefreshKey = privateRefreshKey;
        this.roleMapper = roleMapper;
        this.expirationAccess = expirationAccess;
    }

    @SneakyThrows
    public String generateAccessToken(@NonNull UserDto userDto) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationIntstant = now.plusMinutes(expirationAccess).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationIntstant);

        return Jwts.builder()
                .setSubject(userDto.getLogin())
                .setExpiration(accessExpiration)
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(privateAccessKey, SignatureAlgorithm.RS256)
                .claim("firstName", userDto.getFirstName())
                .claim("roles", userDto.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet()))
                .compact();

    }

    public String generateRefreshToken(@NonNull UserDto userDto) {

        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationIntstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationIntstant);
        return Jwts.builder()
                .setSubject(userDto.getLogin())
                .signWith(privateRefreshKey)
                .setExpiration(refreshExpiration)
                .compact();

    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, publicAccessKey);
    }

    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken, publicRefreshKey);
    }

    private boolean validateToken(@NonNull String token, Key secret) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;

    }

    public Claims getAccessClaims(@NonNull String accessToken) {
        return getClaims(accessToken, publicAccessKey);
    }

    public Claims getRefreshClaims(@NonNull String refreshToken) {
        return getClaims(refreshToken, publicRefreshKey);
    }

    private Claims getClaims(@NonNull String token, Key secret) {

        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

}
