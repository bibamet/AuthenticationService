package com.example.authenticationservice.config.redis.repo;

import com.example.authenticationservice.config.redis.dto.Model;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataRedisRepo extends KeyValueRepository<Model, String> {
}
