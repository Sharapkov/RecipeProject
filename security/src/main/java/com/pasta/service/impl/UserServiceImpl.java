package com.pasta.service.impl;

import com.pasta.dto.UserDto;
import com.pasta.dto.request.RegistrationRequest;
import com.pasta.dto.response.LoginResponse;
import com.pasta.enumerations.RoleEnum;
import com.pasta.exception.*;
import com.pasta.mapper.UserMapper;
import com.pasta.model.Role;
import com.pasta.model.Token;
import com.pasta.model.User;
import com.pasta.repository.RoleRepository;
import com.pasta.repository.UserRepository;
import com.pasta.security.TokenProvider;
import com.pasta.security.UserDetailsImpl;
import com.pasta.service.TokenService;
import com.pasta.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private TokenProvider tokenProvider;

    @Override
    public LoginResponse login(String username, String password) {
        User user = userRepository.findOneByEmailIgnoreCase(username).orElseThrow(UserNotFoundException::new);

        boolean isMatch = passwordEncoder.matches(password, user.getPassword());

        if (!isMatch) {
            throw new InvalidPasswordException();
        }

        Set<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName()))
                .collect(Collectors.toSet());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), authorities);

        String accessToken = tokenProvider.generateToken(authentication, tokenProvider.getAccessTokenValidity());
        String refreshToken = tokenProvider.generateToken(authentication, tokenProvider.getRefreshTokenValidity());

        tokenService.save(user.getId(), refreshToken);

        userRepository.save(user);

        UserDto userShortDto = userMapper.toDto(user);

        LoginResponse dto = new LoginResponse();
        dto.setAccessToken(accessToken);
        dto.setRefreshToken(refreshToken);
        dto.setUser(userShortDto);
        return dto;
    }

    @Override
    public Integer logout(String refreshToken) {
        return tokenService.remove(refreshToken);
    }

    @Override
    public LoginResponse refresh(String refreshToken) {

        if (refreshToken == null) {
            throw new UnauthorizedException();
        }

        Token dbToken = tokenService.find(refreshToken);
        if (dbToken == null) {
            throw new UnauthorizedException();
        }

        User user = userRepository.findOneByEmail(tokenProvider.getUsernameFromToken(refreshToken))
                .orElseThrow(UserNotFoundException::new);

        Set<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName()))
                .collect(Collectors.toSet());

        boolean isValid = tokenProvider.validateToken(refreshToken, new UserDetailsImpl(user.getEmail(), user.getPassword(), authorities));

        if (!isValid) {
            throw new UnauthorizedException();
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), authorities);

        String newAccessToken = tokenProvider.generateToken(authentication, tokenProvider.getAccessTokenValidity());
        String newRefreshToken = tokenProvider.generateToken(authentication, tokenProvider.getRefreshTokenValidity());

        tokenService.save(user.getId(), newRefreshToken);

        LoginResponse response = new LoginResponse();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(newRefreshToken);
        response.setUser(userMapper.toDto(user));

        return response;
    }

    @Override
    public UserDto register(RegistrationRequest request) {
        userRepository.findOneByEmailIgnoreCase(request.getEmail()).ifPresent(existingUser -> {
            throw new EmailAlreadyUsedException();
        });

        User entity = new User();
        entity.setFirstname(request.getFirstname());
        entity.setLastname(request.getLastname());
        entity.setEmail(request.getEmail());
        entity.setPassword(generateEncodedPassword(request.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findOneByName(RoleEnum.USER.name()).orElseThrow(RoleNotFoundException::new);
        roles.add(role);
        entity.setRoles(roles);

        userRepository.save(entity);
        return userMapper.toDto(entity);
    }

    private String generateEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
