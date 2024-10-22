package com.pasta.service.impl;

import com.pasta.exception.UserNotFoundException;
import com.pasta.model.Token;
import com.pasta.model.User;
import com.pasta.repository.TokenRepository;
import com.pasta.repository.UserRepository;
import com.pasta.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Token save(UUID userId, String refreshToken) {
        Optional<Token> token = tokenRepository.findByUserId(userId);

        if (token.isPresent()) {
            token.get().setRefreshToken(refreshToken);
            return tokenRepository.save(token.get());
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return tokenRepository.save(new Token(refreshToken, user));
    }

    @Override
    @Transactional
    public Integer remove(String refreshToken) {
        return tokenRepository.deleteByRefreshToken(refreshToken);
    }

    @Override
    public Token find(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken).orElseThrow(EntityNotFoundException::new);
    }
}
