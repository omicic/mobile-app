package com.developerblog.app.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.developerblog.app.shared.dto.UserDto;

public interface UserService extends UserDetailsService {
	UserDto createUser(UserDto user);
	UserDto getUser(String email);
	UserDto getUserByUserId(String id);
	UserDto updateUser(String id, UserDto userDto);
	UserDto deleteUser(String id);
}
