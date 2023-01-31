package com.example.authenticationservice.dto;

import com.example.authenticationservice.entity.Role;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
public class UserDto {

    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private Set<RoleDto> roles;

}
