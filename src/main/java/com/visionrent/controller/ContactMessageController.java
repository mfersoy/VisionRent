package com.visionrent.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visionrent.domain.ContactMessage;
import com.visionrent.dto.request.ContactMessageRequest;
import com.visionrent.dto.response.VRResponse;
import com.visionrent.mapper.ContactMessageMapper;
import com.visionrent.service.ContactMessageService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/contactmessage")
@AllArgsConstructor
public class ContactMessageController {
	
	private ContactMessageService contactMessageService;

	private ContactMessageMapper contactMessageMapper;
	
	
	@PostMapping("/visitors")
	public ResponseEntity<VRResponse> createMessage(@Valid @RequestBody ContactMessageRequest contactMessageRequest){
		
		ContactMessage contactMessage = contactMessageMapper.contactMessageRequestToContactMessage(contactMessageRequest);
		contactMessageService.saveMessage(contactMessage);
		
		VRResponse response=new VRResponse("ContactMessage successfully create", true);
		
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	
	
	
	
}
