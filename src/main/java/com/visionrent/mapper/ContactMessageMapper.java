package com.visionrent.mapper;

import com.visionrent.dto.ContactMessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.visionrent.domain.ContactMessage;
import com.visionrent.dto.request.ContactMessageRequest;

import java.util.List;

@Mapper(componentModel="spring")
public interface ContactMessageMapper {

	@Mapping(target="id",ignore=true)
	ContactMessage contactMessageRequestToContactMessage(ContactMessageRequest contactMessageRequest);

	ContactMessageDTO contactMessageToDTO(ContactMessage contactMessage);
	List<ContactMessageDTO> map(List<ContactMessage> concatMessageList);
}
