package com.example.authenticationservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(generator = "uuid_gen")
    @GenericGenerator(name = "uuid_gen", strategy = "org.hibernate.id.UUIDGenerator")
    public UUID id;
    @Column(unique = true)
    public String name;
    @ManyToMany(mappedBy = "roles")
    public Set<User> users;

    @Override
    public String getAuthority() {
        return name;
    }

//    public static Role valueOf(String name) {
//        return new Role(name);
//    }

}
