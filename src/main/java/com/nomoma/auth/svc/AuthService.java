package com.nomoma.auth.svc;

import com.nomoma.auth.model.dto.LoginRequest;

public interface AuthService {
    String login(LoginRequest loginRequest);
    void logout(String uuidString);
    void saveRedisAuth(String uuidString, String token);
}
