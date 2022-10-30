package com.visionrent.controller;

import com.visionrent.dto.UserDTO;
import com.visionrent.dto.request.UpdatePasswordRequest;
import com.visionrent.dto.response.ResponseMessage;
import com.visionrent.dto.response.VRResponse;
import com.visionrent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	//http://localhost:8080/user/auth/all
	@GetMapping("/auth/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserDTO>> getAllUsers(){
		List<UserDTO> allUsers = userService.getAllUsers();
		return ResponseEntity.ok(allUsers);
	}
	
	
	//this service could be called, If logged in user wants to get its own user information
	//http://localhost:8080/user
	@GetMapping
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
	public ResponseEntity<UserDTO> getUser(){
		UserDTO userDTO= userService.getPrincipal();
		return ResponseEntity.ok(userDTO);
	}
	
	//http://localhost:8080/user/auth/pages?page=0&size=2&sort=id&direction=ASC
	@GetMapping("/auth/pages")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<UserDTO>> getAllUsersByPage(@RequestParam("page") int page,@RequestParam("size") int size,
																				@RequestParam("sort") String prop,
																				@RequestParam(value="direction",required=false,defaultValue="DESC") Direction direction){
		
		Pageable pageable=PageRequest.of(page, size,Sort.by(direction,prop));
		Page<UserDTO> userDTOPage = userService.getUserPage(pageable);
		return ResponseEntity.ok(userDTOPage);
	}
	
	//http://localhost:8080/user/1/auth
	//Admin create a request to get a user with id
	@GetMapping("/{id}/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
		UserDTO userDTO= userService.getUserById(id);
		return ResponseEntity.ok(userDTO);
	}
	
	
	@PatchMapping("/auth")
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
	public ResponseEntity<VRResponse> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest ){
		userService.updatePassword(updatePasswordRequest);
		
		VRResponse response=new VRResponse();
		response.setMessage(ResponseMessage.PASSWORD_CHANGED_RESPONSE_MESSAGE);
		response.setSucess(true);
		
		return ResponseEntity.ok(response);
		
	}
	
	
	
	
	
	
	
	
	
	
}
