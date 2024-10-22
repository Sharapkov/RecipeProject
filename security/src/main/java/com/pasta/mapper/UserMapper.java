package com.pasta.mapper;

import com.pasta.dto.UserDto;
import com.pasta.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDto, User> {

    UserDto toDto(User entity);

    User toEntity(UserDto dto);

}
