package com.visionrent.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactMessageRequest {

    @NotBlank(message="Please provide a Name")
	private String name;

    @NotBlank(message="Please provide a Name")
	private String subject;
	
    @NotBlank(message="Please provide a Name")
	private String body;
    
    @NotBlank(message="Please provide a Name")
	private String email;

}
