package com.visionrent.controller;

import com.visionrent.domain.ImageFile;
import com.visionrent.dto.ImageFileDTO;
import com.visionrent.dto.response.ImageSavedResponse;
import com.visionrent.dto.response.ResponseMessage;
import com.visionrent.dto.response.VRResponse;
import com.visionrent.service.ImageFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
	public ResponseEntity<byte[]> downloadFile(@PathVariable String id){
		ImageFile imageFile= imageFileService.getImageById(id);
	
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+imageFile.getName()).
				body(imageFile.getImageData().getData());
	}
	
	//http://localhost:8080/files/display/089ff884-00b0-443d-a82c-00a113c683ab
	@GetMapping("/display/{id}")
	public ResponseEntity<byte[]> displayFile(@PathVariable String id){
		ImageFile imageFile = imageFileService.getImageById(id);
		HttpHeaders header=new HttpHeaders();
		header.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<>(imageFile.getImageData().getData(),header,HttpStatus.OK);
	}
	
	
	//http://localhost:8080/files
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ImageFileDTO>> getAllImages(){
	     List<ImageFileDTO> allImageDTO = imageFileService.getAllImages();
	     return ResponseEntity.ok(allImageDTO);
	}
	
	//http://localhost:8080/files/33bb0698-f4b8-4e02-8872-5b14f8538d3a
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<VRResponse> deleteImageFile(@PathVariable String id){
		imageFileService.removeById(id);
		
		VRResponse response=new VRResponse(ResponseMessage.IMAGE_DELETE_RESPONSE_MESSAGE, true);
		return ResponseEntity.ok(response);
	}
	
	
}
