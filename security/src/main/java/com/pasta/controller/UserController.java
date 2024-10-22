package com.pasta.controller;

import com.pasta.dto.UserDto;
import com.pasta.dto.request.LoginRequest;
import com.pasta.dto.request.RegistrationRequest;
import com.pasta.dto.response.LoginResponse;
import com.pasta.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.pasta.util.CookieHelper.addRefreshTokenCookie;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Value("${jwt.token.validity.refresh}")
    private long refreshTokenValidity;

    @Operation(summary = "Авторизация")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse dto = userService.login(request.getUsername(), request.getPasswd());
        if (dto.getStatus() != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dto);
        }
        addRefreshTokenCookie(dto.getRefreshToken(), response, refreshTokenValidity);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Выход")
    @PostMapping("/logout")
    public void logout(@CookieValue("refreshToken") String refreshToken) {
        userService.logout(refreshToken);
    }

    @Operation(summary = "Получить новый access token")
    @GetMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@CookieValue(name = "refreshToken") String refreshToken, HttpServletResponse response) {
        LoginResponse dto = userService.refresh(refreshToken);
        if (dto.getStatus() != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dto);
        }
        addRefreshTokenCookie(dto.getRefreshToken(), response, refreshTokenValidity);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Регистрация")
    @PostMapping("/registration")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegistrationRequest request) {
        UserDto userDto = userService.register(request);
        return ResponseEntity.ok(userDto);
    }
}
