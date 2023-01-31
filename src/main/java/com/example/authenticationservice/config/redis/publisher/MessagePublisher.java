package com.example.authenticationservice.config.redis.publisher;

public interface MessagePublisher {
    void publish(final String message);
}
