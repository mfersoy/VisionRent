package com.visionrent.controller;

import com.visionrent.domain.ImageFile;
import com.visionrent.dto.response.ImageSavedResponse;
import com.visionrent.dto.response.ResponseMessage;
import com.visionrent.service.ImageFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class ImageFileController {

	@Autowired
	private ImageFileService imageFileService;
	
	//http://localhost:8080/files/upload
	@PostMapping("/upload")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ImageSavedResponse> uploadFile(@RequestParam("file") MultipartFile file){
		String imageId= imageFileService.saveImage(file);
		ImageSavedResponse response=new ImageSavedResponse(imageId, ResponseMessage.IMAGE_SAVED_RESPONSE_MESSAGE, true);
		return ResponseEntity.ok(response);
	}
	
	//http://localhost:8080/files/download/089ff884-00b0-443d-a82c-00a113c683ab
	@GetMapping("/download/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<byte[]> downloadFile(@PathVariable String id){
		ImageFile imageFile= imageFileService.getImageById(id);
	
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+imageFile.getName()).
				body(imageFile.getImageData().getData());
	}
	
}
