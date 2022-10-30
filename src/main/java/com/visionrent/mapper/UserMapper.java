package com.visionrent.mapper;

import com.visionrent.domain.User;
import com.visionrent.dto.UserDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface UserMapper {

	UserDTO userToUserDTO(User user);
	
	List<UserDTO> map(List<User> userList);
	
}
