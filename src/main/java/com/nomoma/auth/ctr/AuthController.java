package com.nomoma.auth.ctr;

import java.util.Arrays;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nomoma.auth.model.dto.LoginRequest;
import com.nomoma.auth.svc.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        final String token = authService.login(loginRequest);
        final Cookie keycloakTokenCookie = new Cookie("nomomat", token);
        keycloakTokenCookie.setHttpOnly(true);
//        keycloakTokenCookie.setSecure(true);
        response.addCookie(keycloakTokenCookie);

        final String uuidString = UUID.randomUUID().toString();
        final Cookie uuidCookie = new Cookie("nomomau", uuidString);
        uuidCookie.setHttpOnly(true);
        response.addCookie(uuidCookie);

        authService.saveRedisAuth(uuidString, token);
        return ResponseEntity.ok().body("logged in");
    }

    @GetMapping("/auth/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        final Cookie uuidCookie = Arrays.stream(request.getCookies())
                                        .filter(c->c.getName().equalsIgnoreCase("nomomau"))
                                        .findFirst().orElseThrow();

        authService.logout(uuidCookie.getValue());
        return ResponseEntity.ok().body("logged out");
    }

    @GetMapping("/user/hello")
    public ResponseEntity<String> helloUser() {
        return ResponseEntity.ok("Hello user");
    }
    @GetMapping("/admin/test")
    public ResponseEntity<String> helloAdmin() {
        return ResponseEntity.ok("hello admin");
    }
}
