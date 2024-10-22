package com.pasta.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RoleDto {

    private UUID id;
    private String name;
    private List<PermissionDto> accesses;

}
