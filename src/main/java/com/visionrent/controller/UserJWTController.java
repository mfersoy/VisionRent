package com.visionrent.controller;

import com.visionrent.dto.request.RegisterRequest;
import com.visionrent.dto.response.VRResponse;
import com.visionrent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserJWTController {

	@Autowired
	private UserService userService;
	
	
	@PostMapping("/register")
	public ResponseEntity<VRResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
		return null;
	}
	
	
}
