package com.example.authenticationservice.config.redis.dto;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Model {
    @Id
    public String id;
    public String login;
    public String token;
}
