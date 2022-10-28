package com.visionrent.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.visionrent.domain.ContactMessage;
import com.visionrent.repository.ContactMessageRepository;

import lombok.AllArgsConstructor;

import java.util.List;

@Service
@AllArgsConstructor
public class ContactMessageService  {

private ContactMessageRepository contactMessageRepository;	
	
public void saveMessage(ContactMessage contactMessage) {
	contactMessageRepository.save(contactMessage);
}

	public List<ContactMessage> getAll(){
		return contactMessageRepository.findAll();
	}

	public Page<ContactMessage> getAll(Pageable pageable){
		return contactMessageRepository.findAll(pageable);
	}

}
