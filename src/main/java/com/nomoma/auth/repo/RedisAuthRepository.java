package com.nomoma.auth.repo;

import org.springframework.data.repository.CrudRepository;

import com.nomoma.auth.model.entity.RedisAuth;

public interface RedisAuthRepository extends CrudRepository<RedisAuth, String> {
}
