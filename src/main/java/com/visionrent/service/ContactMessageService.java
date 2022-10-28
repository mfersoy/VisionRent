package com.visionrent.service;

import com.visionrent.exception.ResourceNotFoundException;
import com.visionrent.exception.message.ErrorMessage;
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

	public ContactMessage getContactMessage(Long id) {
		ContactMessage contactMessage = contactMessageRepository.findById(id).orElseThrow(()->
				new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, id)));
		return contactMessage;
	}

	public void deleteContactMessage(Long id) {
		ContactMessage message= getContactMessage(id);
		contactMessageRepository.delete(message);
	}

}
