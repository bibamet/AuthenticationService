package com.example.authenticationservice.controller;

import com.example.authenticationservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class TestController {

    private final AuthService authService;

    @ResponseBody
    @GetMapping("/hello/user")
    @PreAuthorize("hasAuthority('USER')")
    public String helloUser() {
        return "Привет, " + authService.getAuthInfo().getUsername();
    }

    @ResponseBody
    @GetMapping("/hello/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String helloAdmin() {
        return "Привет, " + authService.getAuthInfo().getUsername();
    }

}
