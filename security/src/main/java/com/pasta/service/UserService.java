package com.pasta.service;

import com.pasta.dto.UserDto;
import com.pasta.dto.request.RegistrationRequest;
import com.pasta.dto.response.LoginResponse;

public interface UserService {
    LoginResponse login(String email, String password);

    Integer logout(String refreshToken);

    LoginResponse refresh(String refreshToken);

    UserDto register(RegistrationRequest request);
}
