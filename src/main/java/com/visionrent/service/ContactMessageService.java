package com.visionrent.service;

import org.springframework.stereotype.Service;

import com.visionrent.domain.ContactMessage;
import com.visionrent.repository.ContactMessageRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ContactMessageService  {

private ContactMessageRepository contactMessageRepository;	
	
public void saveMessage(ContactMessage contactMessage) {
	contactMessageRepository.save(contactMessage);
}

}
