package com.example.authenticationservice.utils;

import com.example.authenticationservice.dto.JwtAuthentication;
import com.example.authenticationservice.dto.RoleDto;
import com.example.authenticationservice.entity.Role;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setFirstName(claims.get("firstName", String.class));
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }

    private static Set<RoleDto> getRoles(Claims claims) {
        final List<String> roles = claims.get("roles", List.class);
        Set<RoleDto> roleList = new HashSet<>();
        roles.forEach(role -> {
            RoleDto role1 = new RoleDto();
            role1.setName(role);
            roleList.add(role1);
        });
        return roleList;
    }

}
