package com.example.authenticationservice.config.redis.repo;

import com.example.authenticationservice.config.redis.dto.Model;

import java.time.Duration;

public interface RedisRepository {
    void add(String key, Model model, Duration duration);
    void delete(String key,String login);
    String findToken(String key,String login);
}
