package com.visionrent.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.visionrent.domain.ContactMessage;
import com.visionrent.dto.request.ContactMessageRequest;

@Mapper(componentModel="spring")
public interface ContactMessageMapper {

	@Mapping(target="id",ignore=true)
	ContactMessage contactMessageRequestToContactMessage(ContactMessageRequest contactMessageRequest);
	
}
