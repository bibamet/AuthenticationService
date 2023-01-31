package com.example.authenticationservice.controller;

import com.example.authenticationservice.jwtprovider.JwtProvider;
import com.example.authenticationservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tokens")
@RequiredArgsConstructor
public class TokenController {

    private final JwtProvider jwtProvider;
    private final AuthService authService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public boolean isRevoked(@RequestParam(name = "tokenn") String token) {
        return authService.isRevokedAccess(token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/access")
    @PreAuthorize("hasAuthority('ADMIN')")
    public boolean revokeAccess(@Param("token") String token) {
        return authService.revokeAccessToken(token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/refresh")
    @PreAuthorize("hasAuthority('ADMIN')")
    public boolean revokeRefresh(@Param("token") String token) {
        return authService.revokeRefreshToken(token);
    }

}
