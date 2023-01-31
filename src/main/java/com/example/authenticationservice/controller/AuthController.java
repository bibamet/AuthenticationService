package com.example.authenticationservice.controller;

import com.example.authenticationservice.dto.JwtRefreshRequest;
import com.example.authenticationservice.dto.JwtRequest;
import com.example.authenticationservice.dto.JwtResponse;
import com.example.authenticationservice.service.AuthService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse login(@RequestBody JwtRequest jwtRequest) {
        return authService.login(jwtRequest);
    }

    @PostMapping("token")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse getNewAccessToken(@NonNull @RequestBody JwtRefreshRequest jwtRefreshRequest) {
        return authService.getAccessToken(jwtRefreshRequest.getRefreshToken());
    }

    @PostMapping("refresh")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse getNewRefreshToken(@NonNull @RequestBody JwtRefreshRequest jwtRefreshRequest) {
        return authService.refresh(jwtRefreshRequest.getRefreshToken());
    }

}
