package com.example.authenticationservice.mapper;

import com.example.authenticationservice.dto.UserDto;
import com.example.authenticationservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {

    RoleMapper instanceRoleMapper = Mappers.getMapper(RoleMapper.class);

    @Mapping(target = "roles", expression = "java(instanceRoleMapper.toSetRole(userDto.getRoles()))")
    public User toUser(UserDto userDto);
    @Mapping(target = "roles", expression = "java(instanceRoleMapper.toSetRoleDto(user.getRoles()))")
    public UserDto toUserDto(User user);

}
