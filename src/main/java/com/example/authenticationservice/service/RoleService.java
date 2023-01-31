package com.example.authenticationservice.service;

import com.example.authenticationservice.entity.Role;
import com.example.authenticationservice.repository.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepo roleRepo;

    public Optional<Role> findRoleByName(String name) {
        return roleRepo.findByName(name);
    }

    public Set<Role> saveRoles(Set<Role> roleSet) {
        return roleRepo.saveAll(roleSet).stream().collect(Collectors.toSet());
    }


}
