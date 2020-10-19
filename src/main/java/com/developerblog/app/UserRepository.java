package com.developerblog.app;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.developerblog.app.io.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
	
}
