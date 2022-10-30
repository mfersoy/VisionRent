package com.visionrent.dto;

import com.visionrent.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

	private Long id;
	
	private String firstName;
	
	private String lastName;

	private String email;

	private String phoneNumber; 
	

	private String address; 
	

	private String zipCode; 
	

	private Boolean builtIn;
	
	//ROLE_CUSTOMER,ROLE_ADMIN->Customer,Administrator

	private Set<String> roles;
	
	
	public void setRoles(Set<Role> roles) {
		Set<String> roleStr=new HashSet<>();
		
		roles.forEach(r->{
			roleStr.add(r.getType().getName());
		});
		
		this.roles=roleStr;
	}
	
	
	
}
