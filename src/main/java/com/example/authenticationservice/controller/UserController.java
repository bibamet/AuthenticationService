package com.example.authenticationservice.controller;

import com.example.authenticationservice.dto.UserDto;
import com.example.authenticationservice.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserDto createUser(@NonNull @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserDto createAdmin(@NonNull @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }


//    @ResponseBody
//    @ResponseStatus(HttpStatus.OK)
//    @GetMapping("/test/{user}")
//    public Object test(@PathVariable(name = "user") String login) {
//        return userService.testClaims(login);
//    }
}
