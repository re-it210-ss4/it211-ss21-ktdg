package com.example.base_spring_boot.models.repositories;

import com.example.base_spring_boot.models.entities.RefreshToken;
import com.example.base_spring_boot.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);

}