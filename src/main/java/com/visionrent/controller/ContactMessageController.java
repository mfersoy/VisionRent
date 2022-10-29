package com.visionrent.controller;

import javax.validation.Valid;

import com.visionrent.dto.ContactMessageDTO;
import com.visionrent.dto.response.ResponseMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.visionrent.domain.ContactMessage;
import com.visionrent.dto.request.ContactMessageRequest;
import com.visionrent.dto.response.VRResponse;
import com.visionrent.mapper.ContactMessageMapper;
import com.visionrent.service.ContactMessageService;

import lombok.AllArgsConstructor;

import java.util.List;

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
		
		VRResponse response=new VRResponse(ResponseMessage.CONTACTMESSAGE_SAVE_RESPONSE_MESSAGE, true);
		
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<ContactMessageDTO>> getAllContactMessage(){
		List<ContactMessage> contactMessageList = contactMessageService.getAll();
		List<ContactMessageDTO> contactMessageDTOList = contactMessageMapper.map(contactMessageList);
		return ResponseEntity.ok(contactMessageDTOList);
	}

	@GetMapping("/pages")
	public ResponseEntity<Page<ContactMessageDTO>> getAllContactMessageWithPage(@RequestParam("page") int page, @RequestParam("size") int size,
																				@RequestParam("sort") String prop,
																				@RequestParam(value="direction",required=false,defaultValue="DESC") Sort.Direction direction){

		Pageable pageable= PageRequest.of(page, size,Sort.by(direction,prop));

		Page<ContactMessage> contactMessagePage = contactMessageService.getAll(pageable);

		Page<ContactMessageDTO> pageDTO = getPageDTO(contactMessagePage);

		return ResponseEntity.ok(pageDTO);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ContactMessageDTO> getMessageWithPath(@PathVariable("id") Long id){
		ContactMessage contactMessage = contactMessageService.getContactMessage(id);
		ContactMessageDTO contactMessageDTO = contactMessageMapper.contactMessageToDTO(contactMessage);
		return ResponseEntity.ok(contactMessageDTO);
	}

	@GetMapping("/request")
	public ResponseEntity<ContactMessageDTO> getMessageWithRequestParam(@RequestParam("id") Long id){
		ContactMessage contactMessage = contactMessageService.getContactMessage(id);
		ContactMessageDTO contactMessageDTO = contactMessageMapper.contactMessageToDTO(contactMessage);
		return ResponseEntity.ok(contactMessageDTO);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<VRResponse> deleteContactMessage(@PathVariable Long id){
		contactMessageService.deleteContactMessage(id);

		VRResponse vrResponse=new VRResponse(ResponseMessage.CONTACTMESSAGE_DELETE_RESPONSE_MESSAGE, true);
		return ResponseEntity.ok(vrResponse);
	}

	@PutMapping("/{id}")
	public ResponseEntity<VRResponse> updateContactMessage(@PathVariable Long id, @Valid
	@RequestBody ContactMessageRequest contactMessageRequest) {

		ContactMessage contactMessage = contactMessageMapper.contactMessageRequestToContactMessage(contactMessageRequest);
		contactMessageService.updateContactMessage(id,contactMessage);

		VRResponse vrResponse=new VRResponse(ResponseMessage.CONTACTMESSAGE_UPDATE_RESPONSE_MESSAGE, true);
		return ResponseEntity.ok(vrResponse);

	}




	private Page<ContactMessageDTO> getPageDTO(Page<ContactMessage> contactMessagePage){

		Page<ContactMessageDTO> dtoPage= contactMessagePage.map(new java.util.function.Function<ContactMessage, ContactMessageDTO>() {
			@Override
			public ContactMessageDTO apply(ContactMessage contactMessage) {
				return contactMessageMapper.contactMessageToDTO(contactMessage);
			}
		});
		return dtoPage;
	}
	
	
	
	
}
