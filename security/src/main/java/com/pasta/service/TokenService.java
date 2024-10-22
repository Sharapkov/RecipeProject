package com.pasta.service;

import com.pasta.model.Token;

import java.util.UUID;

public interface TokenService {

    Token save(UUID userId, String refreshToken);

    Integer remove(String refreshToken);

    Token find(String refreshToken);

}