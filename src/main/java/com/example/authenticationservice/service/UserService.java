package com.example.authenticationservice.service;

import com.example.authenticationservice.dto.UserDto;
import com.example.authenticationservice.entity.User;
import com.example.authenticationservice.entity.Role;
import com.example.authenticationservice.jwtprovider.JwtProvider;
import com.example.authenticationservice.mapper.UserMapper;
import com.example.authenticationservice.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final List<User> users;
    private final RoleService roleService;
    private final JwtProvider jwtProvider;

//    public UserService(List<User> users) {
//        this.users = List.of(
//                User.builder()
//                        .login("bibamet")
//                        .password("130996")
//                        .firstName("Pavel")
//                        .lastName("Kalinchuk")
//                        .roles(Collections.singleton(Role.USER))
//                        .build(),
//                User.builder()
//                        .login("anyutka")
//                        .password("260496")
//                        .firstName("Anna")
//                        .lastName("Kalinchuk")
//                        .roles(Collections.singleton(Role.ADMIN))
//                        .build()
//        );
//    }

    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        Set<Role> roles = user.getRoles();
        Set<Role> findedRoles = new HashSet<>();
        roles.forEach(role -> findedRoles.add(roleService.findRoleByName(role.getName()).orElseThrow(() -> new EntityNotFoundException(String.format("Роль %s не найдена", role.getName())))));
        user.setRoles(findedRoles);
        return userMapper.toUserDto(userRepo.save(user));
    }

    public Optional<User> getByLogin(@NonNull String login) {
        return userRepo.findByLogin(login);
    }

//    public Object testClaims(String login) {
//        Optional<User> userOptional = getByLogin(login);
//        User user = userOptional.isEmpty() ? new User() : userOptional.get();
//        UserDto userDto = userMapper.toUserDto(user);
//        return jwtProvider.test(userDto);
//    }

}
