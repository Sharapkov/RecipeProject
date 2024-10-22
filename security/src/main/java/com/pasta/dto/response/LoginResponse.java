package com.pasta.dto.response;

import com.pasta.dto.UserDto;
import lombok.Data;

@Data
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private String status;
    private UserDto user;

}
