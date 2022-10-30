package com.visionrent.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.visionrent.dto.UserDTO;
import com.visionrent.dto.request.AdminUserUpdateRequest;
import com.visionrent.dto.request.UpdatePasswordRequest;
import com.visionrent.dto.request.UserUpdateRequest;
import com.visionrent.dto.response.ResponseMessage;
import com.visionrent.dto.response.VRResponse;
import com.visionrent.service.UserService;

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

	/*
	 * {
     "oldPassword":"admin",
     "newPassword":"admin1"
     }
	 */

	//	http://localhost:8080/user/auth
	@PatchMapping("/auth")
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
	public ResponseEntity<VRResponse> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest ){
		userService.updatePassword(updatePasswordRequest);

		VRResponse response=new VRResponse();
		response.setMessage(ResponseMessage.PASSWORD_CHANGED_RESPONSE_MESSAGE);
		response.setSucess(true);

		return ResponseEntity.ok(response);

	}

	/*
	 * {
    "firstName": "transaction",
    "lastName": "admin1",
    "phoneNumber": "(555) 555-8828",
    "email": "transaction@email.com",
    "address": "New Jersey,US",
    "zipCode": "79877"
}
	 */

	//http://localhost:8080/user
	@PutMapping
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
	public ResponseEntity<VRResponse> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest  ){
		userService.updateUser(userUpdateRequest);

		VRResponse response=new VRResponse();
		response.setMessage(ResponseMessage.USER_UPDATE_RESPONSE_MESSAGE);
		response.setSucess(true);

		return ResponseEntity.ok(response);

	}



	/*
	 * {
        "firstName": "Bruce",
        "lastName": "Wayne",
        "email": "bruce@email.com",
        "phoneNumber": "(555) 444-8828",
        "address": "New Jersey,US",
        "zipCode": "65421",
        "builtIn": false,
        "roles": [
            "Customer",
            "Administrator"
        ]
    }
	 */
	//http://localhost:8080/user/3/auth
	@PutMapping("/{id}/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<VRResponse> updateUserAuth(@PathVariable Long id,  @Valid @RequestBody AdminUserUpdateRequest adminUserUpdateRequest){
		userService.updateUserAuth(id, adminUserUpdateRequest);

		VRResponse response=new VRResponse();
		response.setMessage(ResponseMessage.USER_UPDATE_RESPONSE_MESSAGE);
		response.setSucess(true);

		return ResponseEntity.ok(response);
	}

	//http://localhost:8080/user/5/auth
	@DeleteMapping("/{id}/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<VRResponse> deleteUser(@PathVariable Long id){
		userService.removeUserById(id);

		VRResponse response=new VRResponse();
		response.setMessage(ResponseMessage.USER_DELETE_RESPONSE_MESSAGE);
		response.setSucess(true);

		return ResponseEntity.ok(response);
	}







}
