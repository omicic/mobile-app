package com.developerblog.app.ui;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.developerblog.app.services.UserService;
import com.developerblog.app.shared.dto.UserDto;
import com.developerblog.app.ui.model.request.UserDetailsRequestModel;
import com.developerblog.app.ui.model.response.UserResp;

@RestController
@RequestMapping("/users") //http://localhost:8080/users
public class UsersController {
	
	@Autowired
	UserService userService;

	@GetMapping(path = "/{id}")
	public UserResp getUser(@PathVariable String id) {
		UserResp returnValue = new UserResp();
		UserDto userDto = userService.getUserByUserId(id);
		
		BeanUtils.copyProperties(userDto, returnValue);
		
		return returnValue;
	}
	
	@PostMapping
	public UserResp createUser(@RequestBody UserDetailsRequestModel userDetails) {
		
		//userDetail as RequestModel-->copy to userDto-->returnValue as UserRest
		UserResp returnValue = new UserResp();
		UserDto userDto = new UserDto();
		
		BeanUtils.copyProperties(userDetails, userDto);
		UserDto createdUser = userService.createUser(userDto);
		
		BeanUtils.copyProperties(createdUser, returnValue);
		return returnValue;		
	}
	
	@PutMapping
	public String updateUser() {
		return "Update user je pozvan";		
	}
	
	@DeleteMapping
	public String deleteUser() {
		return "Delete user je pozvan";		
	}

}
