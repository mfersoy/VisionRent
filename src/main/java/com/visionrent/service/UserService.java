package com.visionrent.service;

import com.visionrent.domain.Role;
import com.visionrent.domain.User;
import com.visionrent.domain.enums.RoleType;
import com.visionrent.dto.request.RegisterRequest;
import com.visionrent.exception.ConflictException;
import com.visionrent.exception.ResourceNotFoundException;
import com.visionrent.exception.message.ErrorMessage;
import com.visionrent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
	
	  @Autowired
	  private UserRepository userRepository;
	  
	  @Autowired
	  private RoleService roleService;
	  
	  @Autowired
	  private PasswordEncoder passwordEncoder;
	  
	  public void saveUser(RegisterRequest registerRequest) {
		  if(userRepository.existsByEmail(registerRequest.getEmail())) {
			  throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE, registerRequest.getEmail()));
		  }
		  
		  
		  Role role= roleService.findByType(RoleType.ROLE_CUSTOMER);
		  
		  Set<Role> roles=new HashSet<>();
		  roles.add(role);
		  
		  User user =new User();
		  user.setFistName(registerRequest.getFistName());
		  //TODO:we will set other variables for user.
		  
	  }
	  
	  public User getUserByEmail(String email) {
		  User user = userRepository.findByEmail(email).orElseThrow(()->new 
				  ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_MESSAGE, email)));
	      return user;
	  }
}
