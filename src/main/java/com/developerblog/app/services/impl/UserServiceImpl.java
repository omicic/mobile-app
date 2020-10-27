package com.developerblog.app.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.developerblog.app.exception.UserServiceException;
import com.developerblog.app.io.entity.UserEntity;
import com.developerblog.app.io.repositories.UserRepository;
import com.developerblog.app.services.UserService;
import com.developerblog.app.shared.Utils;
import com.developerblog.app.shared.dto.UserDto;
import com.developerblog.app.ui.model.response.ErrorMessages;
import com.fasterxml.jackson.databind.BeanProperty;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public UserDto createUser(UserDto user) {
		if(userRepository.findByEmail(user.getEmail()) != null) 
			throw new RuntimeException("Record already exists");
					
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		
		String publicUserId = utils.generateUserId(30);
		userEntity.setUserId(publicUserId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		
		
		UserEntity storedUserDetails = userRepository.save(userEntity);
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetails,returnValue);
		return returnValue;
	}

	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if(userEntity == null) throw new UsernameNotFoundException(email);
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList());
	}

	public UserDto getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		
		if(userEntity == null) throw new UsernameNotFoundException(email);
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		
		return returnValue;
		
	}

	public UserDto getUserByUserId(String id) {
		UserEntity userEntity = userRepository.findByUserId(id);
		
		if(userEntity == null) throw new UsernameNotFoundException(id);
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		
		return returnValue;
	}

	public UserDto updateUser(String id, UserDto userDto) {
		
		UserDto returnValue = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(id);
		
		if(userEntity == null) throw new UsernameNotFoundException(id);
		
		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());
			
		UserEntity updatedUserDetails = userRepository.save(userEntity);
		

		BeanUtils.copyProperties(updatedUserDetails,returnValue );
		return returnValue;
	}

	
	public UserDto deleteUser(String id) {
		UserEntity userEntity = userRepository.findByUserId(id);
		if (userEntity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

		userRepository.delete(userEntity);
		return null;
	}

	public List<UserDto> getUsers(int page, int limit) {
		
		
	
		List<UserDto> returnValue = new ArrayList<UserDto>();
		if(page>0) page = page-1;
		
		Pageable pageableRequest = PageRequest.of(page, limit);
		
	
		Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
		List<UserEntity> users = usersPage.getContent();
		
		for(UserEntity userEntity : users) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(userEntity, userDto);
			returnValue.add(userDto);
		}
		
		return returnValue;
	}

}
