package com.pasta.dto;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String email;
    private String firstname;
    private String lastname;
    private Set<RoleDto> roles;
}
