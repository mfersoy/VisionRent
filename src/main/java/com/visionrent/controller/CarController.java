package com.visionrent.controller;

import com.visionrent.dto.CarDTO;
import com.visionrent.dto.response.ResponseMessage;
import com.visionrent.dto.response.VRResponse;
import com.visionrent.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {
	
	@Autowired
	private CarService carService;
	
	
	/*
	 *  {
    "model": "cupra",
    "doors": 4,
    "seats": 4,
    "luggage": 450,
    "transmission": "automatic",
    "airConditioning": true,
    "age": 4,
    "pricePerHour": 400,
    "fuelType": "diesel"
}
	 */
	
	//http://localhost:8080/car/admin/ffbd1a2f-301f-4d78-bd3b-238351caa558/add
	
	@PostMapping("/admin/{imageId}/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<VRResponse> saveCar(@PathVariable String imageId, @Valid @RequestBody CarDTO carDTO){
		carService.saveCar(imageId, carDTO);
		
		VRResponse response=new VRResponse(ResponseMessage.CAR_SAVED_RESPONSE_MESSAGE,true);
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	
	//http://localhost:8080/car/visitors/all
	@GetMapping("/visitors/all")
	public ResponseEntity<List<CarDTO>> getAllCars(){
		List<CarDTO> allCars = carService.getAllCars();
		return ResponseEntity.ok(allCars);
	}
	
	//http://localhost:8080/car/visitors/pages?size=2&page=0&sort=id&direction=DESC
	
	@GetMapping("/visitors/pages")
	public ResponseEntity<Page<CarDTO>> getAllCarsWithPage(@RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam("sort") String prop,
			@RequestParam(value="direction",required=false,defaultValue="DESC") Direction direction){
		
		Pageable pageable= PageRequest.of(page, size, Sort.by(direction,prop));
		
		 Page<CarDTO> pageDTO = carService.findAllWithPage(pageable);
	
		 return ResponseEntity.ok(pageDTO);
	}
	
	//http://localhost:8080/car/visitors/1
	@GetMapping("/visitors/{id}")
	public ResponseEntity<CarDTO> getCarById(@PathVariable Long id){
		CarDTO carDTO= carService.findById(id);
		return ResponseEntity.ok(carDTO);
	}
	
	
	/*
	 * {
    "model": "audi a4",
    "doors": 4,
    "seats": 4,
    "luggage": 500,
    "transmission": "manual",
    "airConditioning": true,
    "age": 4,
    "pricePerHour": 800,
    "fuelType": "electricity"
}
	 */
	
	//http://localhost:8080/car/admin/auth?id=3&imageId=0291f66e-bc60-4562-9185-1fda9c1aa8ec
	
	@PutMapping("/admin/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<VRResponse> updateCar(@RequestParam("id") Long id,@RequestParam("imageId") String imageId,
			@Valid @RequestBody CarDTO carDTO){
		carService.updateCar(id, imageId, carDTO);
		
		VRResponse response=new VRResponse(ResponseMessage.CAR_UPDATE_RESPONSE_MESSAGE,true);
		return ResponseEntity.ok(response);
	}
	
	//http://localhost:8080/car/admin/2/auth
	@DeleteMapping("/admin/{id}/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<VRResponse> deleteCar(@PathVariable Long id){
		carService.removeById(id);
        
		VRResponse response=new VRResponse(ResponseMessage.CAR_DELETE_RESPONSE_MESSAGE,true);
		return ResponseEntity.ok(response);
	}
	
	
	
	
	

}
