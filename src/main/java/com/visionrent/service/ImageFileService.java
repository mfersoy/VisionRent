package com.visionrent.service;

import com.visionrent.domain.ImageData;
import com.visionrent.domain.ImageFile;
import com.visionrent.exception.ResourceNotFoundException;
import com.visionrent.exception.message.ErrorMessage;
import com.visionrent.repository.ImageFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
public class ImageFileService {

	@Autowired
	private ImageFileRepository imageFileRepository;
	
	public String saveImage(MultipartFile file) {
		String fileName=StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
		ImageFile imageFile=null;
		
		try {
			ImageData imData=new ImageData(file.getBytes());
			imageFile=new ImageFile(fileName, file.getContentType(), imData);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		
		imageFileRepository.save(imageFile);
		return imageFile.getId();
	}
	
	public ImageFile getImageById(String imageId) {
		ImageFile imageFile=imageFileRepository.findById(imageId).orElseThrow(()->new 
				ResourceNotFoundException(String.format(ErrorMessage.IMAGE_NOT_FOUND_MESSAGE, imageId)));
		return imageFile;
	}
	
	
}
