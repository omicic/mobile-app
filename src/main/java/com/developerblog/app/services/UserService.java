package com.developerblog.app.services;

import com.developerblog.app.shared.dto.UserDto;

public interface UserService {
	UserDto createUser(UserDto user);
}
