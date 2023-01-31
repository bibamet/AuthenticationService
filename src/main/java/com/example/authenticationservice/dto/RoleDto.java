package com.example.authenticationservice.dto;

import com.example.authenticationservice.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto implements GrantedAuthority {

    public String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
