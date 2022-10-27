package com.nomoma.auth.svc.impl;

import org.keycloak.admin.client.Keycloak;
import org.springframework.stereotype.Service;

import com.nomoma.auth.model.dto.LoginRequest;
import com.nomoma.auth.model.entity.RedisAuth;
import com.nomoma.auth.repo.RedisAuthRepository;
import com.nomoma.auth.svc.AuthService;
import com.nomoma.config.KeycloakProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final KeycloakProvider keycloakProvider;
    private final RedisAuthRepository redisAuthRepository;

    @Override
    public String login(LoginRequest loginRequest) {
        Keycloak keycloak = keycloakProvider.Keycloak(loginRequest.getUsername(), loginRequest.getPassword());
        String token = keycloak.tokenManager().getAccessToken().getToken();
        return token;
    }

    @Override
    public void saveRedisAuth(String uuidString, String token) {
        RedisAuth redisAuth = new RedisAuth(uuidString, token, false);
        redisAuthRepository.save(redisAuth);
    }

    @Override
    public void logout(String uuidString) {
        final RedisAuth redisAuth = redisAuthRepository.findById(uuidString).orElseThrow();
        redisAuth.setLoggedOut(true);
        redisAuthRepository.save(redisAuth);
    }
}
