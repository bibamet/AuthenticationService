package com.example.authenticationservice.config.redis.repo;

import com.example.authenticationservice.config.redis.dto.Model;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {

    private final RedisTemplate redisTemplate;
    private HashOperations hashOperations;
    private ValueOperations redisOperations;

    @PostConstruct
    void init() {
        this.hashOperations = redisTemplate.opsForHash();
        this.redisOperations = redisTemplate.opsForValue();
    }

    @Override
    public void add(String key, Model model, Duration duration) {
//        hashOperations.put(key, model.getLogin(), model.getToken());
        redisOperations.set(model.getLogin() + ":" + key, model.getToken(), duration);
    }

    @Override
    public void delete(String key, String login) {
//        hashOperations.delete(key, login);
        redisOperations.getAndDelete(login + ":" + key);
    }

    @Override
    public String findToken(String key, String login) {
//        return (String) hashOperations.get(key, login);
        return (String) redisOperations.get(login + ":" + key);
    }
}
