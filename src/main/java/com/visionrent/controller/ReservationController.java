package com.visionrent.controller;

import com.visionrent.domain.Car;
import com.visionrent.domain.User;
import com.visionrent.dto.ReservationDTO;
import com.visionrent.dto.request.ReservationRequest;
import com.visionrent.dto.request.ReservationUpdateRequest;
import com.visionrent.dto.response.CarAvailabilityResponse;
import com.visionrent.dto.response.ResponseMessage;
import com.visionrent.dto.response.VRResponse;
import com.visionrent.service.CarService;
import com.visionrent.service.ReservationService;
import com.visionrent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private UserService userService;
	
	
	@Autowired
	private CarService carService;
	
	
	@PostMapping("/add")
	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	public ResponseEntity<VRResponse> makeReservation(@RequestParam("carId") Long carId,@Valid @RequestBody ReservationRequest reservationRequest){
		Car car= carService.getCarById(carId);
		User user=userService.getCurrentUser();
		
		reservationService.createReservation(reservationRequest, user, car);
		
		VRResponse response=new VRResponse(ResponseMessage.RESERVATION_CREATED_RESPONSE_MESSAGE,true);
		return new ResponseEntity<>(response,HttpStatus.CREATED);
		
	}
	
	@PostMapping("/add/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<VRResponse> addReservation(@RequestParam("userId") Long userId,@RequestParam("carId") Long carId,@Valid @RequestBody ReservationRequest reservationRequest){
		
		Car car= carService.getCarById(carId);
		User user=userService.getById(userId);
		
		reservationService.createReservation(reservationRequest, user, car);
		
		VRResponse response=new VRResponse(ResponseMessage.RESERVATION_CREATED_RESPONSE_MESSAGE,true);
		return new ResponseEntity<>(response,HttpStatus.CREATED);
		
	}
	
	
	@PutMapping("/admin/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<VRResponse> updateReservation(@RequestParam("carId") Long carId, 
			@RequestParam("reservationId") Long reservationId,@Valid @RequestBody ReservationUpdateRequest reservationUpdateRequest ){
		
		Car car= carService.getCarById(carId);
		
		reservationService.updateReservation(reservationId, car, reservationUpdateRequest);
		
		VRResponse response=new VRResponse(ResponseMessage.RESERVATION_UPDATED_RESPONSE_MESSAGE,true);
		return new ResponseEntity<>(response,HttpStatus.OK);
		
		
	}
	
	
	@GetMapping("/auth")
	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	public ResponseEntity<VRResponse> checkCarIsAvailable(@RequestParam("carId") Long carId,
			@RequestParam("pickUpDateTime") @DateTimeFormat(pattern="MM/dd/yyyy HH:mm:ss") LocalDateTime pickUpTime,
			@RequestParam("dropOffDateTime") @DateTimeFormat(pattern="MM/dd/yyyy HH:mm:ss") LocalDateTime dropOffTime){
		
		Car car=carService.getCarById(carId);
		boolean isAvailable=reservationService.checkCarAvailability(car, pickUpTime, dropOffTime);
		Double totalPrice=reservationService.getTotalPrice(car, pickUpTime, dropOffTime);
		
		VRResponse response=new CarAvailabilityResponse(ResponseMessage.CAR_AVAILABLE_MESSAGE,true, isAvailable, totalPrice);
		
		return ResponseEntity.ok(response);
		
	}
			
	
	
	@GetMapping("/admin/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ReservationDTO>> getAllReservations(){
		List<ReservationDTO> allReservations = reservationService.getAllReservations();
		return ResponseEntity.ok(allReservations);
	}
	
	@GetMapping("/admin/all/pages")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<ReservationDTO>> getAllReservationsWithPage(@RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam("sort") String prop,
			@RequestParam(value="direction",required=false,defaultValue="DESC") Direction direction){
		
		Pageable pageable=PageRequest.of(page, size, Sort.by(direction, prop));
		
		Page<ReservationDTO> allReservations = reservationService.getReservationPage(pageable);
		return ResponseEntity.ok(allReservations);
	}
	
	
}
