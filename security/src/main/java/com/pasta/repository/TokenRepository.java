package com.pasta.repository;

import com.pasta.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID>{

    Optional<Token> findByUserId(UUID userId);

    Integer deleteByRefreshToken(String refreshToken);

    Optional<Token> findByRefreshToken(String refreshToken);

}
