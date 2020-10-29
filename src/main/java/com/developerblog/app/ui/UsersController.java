package com.developerblog.app.ui;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.developerblog.app.exception.UserServiceException;
import com.developerblog.app.services.AddressService;
import com.developerblog.app.services.UserService;
import com.developerblog.app.shared.dto.AddressDTO;
import com.developerblog.app.shared.dto.UserDto;
import com.developerblog.app.ui.model.request.RequestOperationName;
import com.developerblog.app.ui.model.request.UserDetailsRequestModel;
import com.developerblog.app.ui.model.response.AddressesRest;
import com.developerblog.app.ui.model.response.ErrorMessages;
import com.developerblog.app.ui.model.response.OperationStatusModel;
import com.developerblog.app.ui.model.response.RequestOperationStatus;
import com.developerblog.app.ui.model.response.UserResp;

@RestController
@RequestMapping("/users") //http://localhost:8080/users
public class UsersController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressesService;

	@GetMapping(path = "/{id}")
	public UserResp getUser(@PathVariable String id) {
		UserResp returnValue = new UserResp();
		UserDto userDto = userService.getUserByUserId(id);
		
		BeanUtils.copyProperties(userDto, returnValue);
		
		return returnValue;
	}
	
	@PostMapping
	public UserResp createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		
		//userDetail as RequestModel-->copy to userDto-->returnValue as UserRest
		if(userDetails.getFirstName().isEmpty())			
				throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
				//example of using handleOtherExceptions method in AppExceptionsHandler.class : all other Exception:  throw new NullPointerException("the null pointer exception");
		UserResp returnValue = new UserResp();
	
		//UserDto userDto = new UserDto();		
		//BeanUtils.copyProperties(userDetails, userDto);
		//using ModelMapper instead
		
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		
		UserDto createdUser = userService.createUser(userDto);
		returnValue = modelMapper.map(createdUser, UserResp.class);
		
		return returnValue;		
	}
	
	@PutMapping(path = "/{id}")
	public UserResp updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		
		UserResp returnValue = new UserResp();
		UserDto userDto = new UserDto();
		
		BeanUtils.copyProperties(userDetails, userDto);
		UserDto updateUser = userService.updateUser(id,userDto);
			
		BeanUtils.copyProperties(updateUser, returnValue);
		return returnValue;	
			
	}
	
	@DeleteMapping(path = "/{id}")
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		
		UserDto deleteUser = userService.deleteUser(id);
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;		
	}
	
	@GetMapping()
	public List<UserResp> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
									@RequestParam(value= "limit", defaultValue = "1") int limit)
	{		
		List<UserResp> returnValue = new ArrayList<UserResp>();
		List<UserDto> users = userService.getUsers(page,limit);
		
		for(UserDto userDto : users) {
			UserResp userModel = new UserResp();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}
		
		return returnValue;
	}
	
	@GetMapping(path = "/{id}/addresses")
	public List<AddressesRest> getAddresses(@PathVariable String id){
		
		List<AddressesRest> returnValue = new ArrayList<AddressesRest>();
		
		List<AddressDTO> addressesDTO = addressesService.getAddresses(id);
		
		if (addressesDTO != null && !addressesDTO.isEmpty()) {
			Type listType = new TypeToken<List<AddressesRest>>() {
			}.getType();
			returnValue = new ModelMapper().map(addressesDTO, listType);

			/*for (AddressesRest addressRest : returnValue) {
				Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(id, addressRest.getAddressId()))
						.withSelfRel();
				addressRest.add(addressLink);

				Link userLink = linkTo(methodOn(UserController.class).getUser(id)).withRel("user");
				addressRest.add(userLink);
			}*/
		}
		
		return returnValue;
	}
	
	
	@GetMapping(path = "/{userId}/addresses/{addressId}")
	public AddressesRest getAddress(@PathVariable String addressId){
	
		AddressDTO addressesDto = addressesService.getAddress(addressId);
		
		ModelMapper modelMapper = new ModelMapper();
		
		AddressesRest addressesRestModel = modelMapper.map(addressesDto, AddressesRest.class);
		
		return addressesRestModel;
		
	}
}
