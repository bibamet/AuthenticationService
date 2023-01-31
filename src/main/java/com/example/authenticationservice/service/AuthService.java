package com.example.authenticationservice.service;

import com.example.authenticationservice.config.redis.dto.Model;
import com.example.authenticationservice.config.redis.repo.RedisRepository;
import com.example.authenticationservice.dto.JwtAuthentication;
import com.example.authenticationservice.dto.JwtRequest;
import com.example.authenticationservice.dto.JwtResponse;
import com.example.authenticationservice.dto.UserDto;
import com.example.authenticationservice.entity.User;
import com.example.authenticationservice.exception.AuthException;
import com.example.authenticationservice.jwtprovider.JwtProvider;
import com.example.authenticationservice.mapper.UserMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final Map<String, String> accessStorage = new HashMap<>();
    private final UserMapper userMapper;
    private final Gson gson;
    private final RedisRepository redisRepository;

    @SneakyThrows
    public JwtResponse login(@NonNull JwtRequest jwtRequest) {
        User user = userService.getByLogin(jwtRequest.getLogin()).orElseThrow(() ->new AuthException("Пользователь не найден"));
        UserDto userDto = userMapper.toUserDto(user);
        if (userDto.getPassword().equals(jwtRequest.getPassword())) {
            String accessToken = jwtProvider.generateAccessToken(userDto);
            String refreshToken = jwtProvider.generateRefreshToken(userDto);
            return JwtResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    @SneakyThrows
    public JwtResponse getAccessToken(@NonNull String refreshToken) {

        if (validateRefreshToken(refreshToken)) {
            Claims refreshClaims = jwtProvider.getRefreshClaims(refreshToken);
            String login = refreshClaims.getSubject();
            User user = userService.getByLogin(login).orElseThrow(() -> new AuthException("Пользователь не найден."));
            UserDto userDto = userMapper.toUserDto(user);
            String newAccessToken = jwtProvider.generateAccessToken(userDto);
            return JwtResponse.builder()
                    .accessToken(newAccessToken)
                    .build();

        }
        return JwtResponse.builder().build();

    }

    @SneakyThrows
    public JwtResponse refresh(@NonNull String refreshToken) {

        if (validateRefreshToken(refreshToken)) {
            Claims refreshClaims = jwtProvider.getRefreshClaims(refreshToken);
            String login = refreshClaims.getSubject();
            User user = userService.getByLogin(login).orElseThrow(() -> new AuthException("Пользователь не найден"));
            UserDto userDto = userMapper.toUserDto(user);
            String newAccessToken = jwtProvider.generateAccessToken(userDto);
            String newRefreshToken = jwtProvider.generateRefreshToken(userDto);
            return JwtResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        }

        throw new AuthException("Невалидный JWT-токен");

    }

    public boolean revokeAccessToken(@NonNull String accessToken) {
        String[] splitedToken = accessToken.split("\\.");
        if (splitedToken.length != 3) return true;
        String decodedBody = new String(Base64.getDecoder().decode(splitedToken[1]));
        Map<String, String> map = mappingTokenBody(decodedBody, Map.class);
        String login = map.get("sub");
        if (login == null) return false;
        redisRepository.add("AccessToken",
                Model.builder()
                        .login(login)
                        .token(accessToken).build(),
                Duration.ofMinutes(5));
        return true;
    }

    public boolean revokeRefreshToken(@NonNull String refreshToken) {
        String[] splitedToken = refreshToken.split("\\.");
        if (splitedToken.length != 3) return true;
        String decodedBody = new String(Base64.getDecoder().decode(splitedToken[1]));
        Map<String, String> map = mappingTokenBody(decodedBody, Map.class);
        String login = map.get("sub");
        if (login == null) return false;
        redisRepository.add("RefreshToken",
                Model.builder()
                        .login(login)
                        .token(refreshToken).build(),
                Duration.ofDays(30));
        return true;
    }

    public boolean isRevokedAccess(String accessToken) {
        String[] splitedToken = accessToken.split("\\.");
        if (splitedToken.length != 3) return true;
        String decodedBody = new String(Base64.getDecoder().decode(splitedToken[1]));
        Map<String, String> bodyMap = mappingTokenBody(decodedBody, Map.class);
        String login = bodyMap.get("sub");
        if (login == null) {
            return true;
        }
        return isRevoked(accessToken, login, "AccessToken");
    }

    public boolean isRevokedRefresh(String refreshToken) {
        String[] splitedToken = refreshToken.split("\\.");
        if (splitedToken.length != 3) return true;
        String decodedBody = new String(Base64.getDecoder().decode(splitedToken[1]));
        Map<String, String> bodyMap = mappingTokenBody(decodedBody, Map.class);
        String login = bodyMap.get("sub");
        if (login == null) {
            return true;
        }
        return isRevoked(refreshToken, login, "RefreshToken");
    }

    private boolean isRevoked(String token, String login, String key) {
        String findedToken = redisRepository.findToken(key, login);
        return findedToken != null && findedToken.equals(token) ? true : false;
    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        return (!isRevokedAccess(accessToken) && jwtProvider.validateAccessToken(accessToken));
    }

    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return (!isRevokedRefresh(refreshToken) && jwtProvider.validateRefreshToken(refreshToken));
    }

    @SneakyThrows
    public <T> T mappingTokenBody(String token, Class<T> clazz) {
        return gson.fromJson(token, clazz);
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }


}
