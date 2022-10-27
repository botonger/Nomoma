package com.nomoma.auth.model.entity;

import java.io.Serializable;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@RedisHash("auth")
public class RedisAuth implements Serializable {
    @Id
    private String id;
    private String token;
    @Setter
    private boolean isLoggedOut;
}
