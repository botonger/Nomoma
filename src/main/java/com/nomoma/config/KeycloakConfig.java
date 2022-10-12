package com.nomoma.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class KeycloakConfig {
    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;
    @Value("${kk.admin.realm}")
    private String adminRealm;
    @Value("${kk.admin.resource}")
    private String adminClient;
    @Value("${kk.admin.username}")
    private String adminUsername;
    @Value("${kk.admin.password}")
    private String adminPassword;

    @Bean
    public KeycloakConfigResolver keycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean(name = "KeycloakAdmin")
    public Keycloak KeycloakAdmin() {
        return KeycloakBuilder.builder().serverUrl(authServerUrl)
                .realm(adminRealm)
                .clientId(adminClient)
                .username(adminUsername)
                .password(adminPassword)
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }
}
