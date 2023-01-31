package com.example.authenticationservice.mapper;

import com.example.authenticationservice.dto.RoleDto;
import com.example.authenticationservice.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name")
    public Role toRole(RoleDto roleDto);
    public Set<Role> toSetRole(Set<RoleDto> roleDtos);
    public RoleDto toRoleDto(Role role);
    public Set<RoleDto> toSetRoleDto(Set<Role> roleSet);

}
