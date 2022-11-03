package com.visionrent.service;

import com.visionrent.domain.Car;
import com.visionrent.domain.Reservation;
import com.visionrent.domain.User;
import com.visionrent.domain.enums.ReservationStatus;
import com.visionrent.dto.ReservationDTO;
import com.visionrent.dto.request.ReservationRequest;
import com.visionrent.dto.request.ReservationUpdateRequest;
import com.visionrent.exception.BadRequestException;
import com.visionrent.exception.ResourceNotFoundException;
import com.visionrent.exception.message.ErrorMessage;
import com.visionrent.mapper.ReservationMapper;
import com.visionrent.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;

@Service
public class ReservationService {

	@Autowired
	private ReservationRepository reservationRepository;
	
	@Autowired 
	private ReservationMapper reservationMapper;
	
	
	
	public List<ReservationDTO> getAllReservations(){
		 List<Reservation> reservations= reservationRepository.findAll();
		 return reservationMapper.map(reservations);
	}
	
	public Page<ReservationDTO> getReservationPage(Pageable pageable){
		Page<Reservation> reservationPage = reservationRepository.findAll(pageable);
		
		Page<ReservationDTO> reservationDTOPage= reservationPage.map(new Function<Reservation, ReservationDTO>() {
			@Override
			public ReservationDTO apply(Reservation reservation) {
				return reservationMapper.reservationToReservationDTO(reservation);
			}
		});
		return reservationDTOPage;
	}
	
	
	private void checkReservationTimeIsCorrect(LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
		LocalDateTime now= LocalDateTime.now();
		
		if(pickUpTime.isBefore(now)) {
			throw new BadRequestException(ErrorMessage.RESERVATION_TIME_INCORRECT_MESSAGE);
		}
		
		boolean isEqual=pickUpTime.isEqual(dropOffTime)?true:false;
		boolean isBefore=pickUpTime.isBefore(dropOffTime)?true:false;
		
		if(isEqual ||!isBefore ) {
			throw new BadRequestException(ErrorMessage.RESERVATION_TIME_INCORRECT_MESSAGE);
		}
		
	}
	
	
	public boolean checkCarAvailability(Car car, LocalDateTime pickUpTime,LocalDateTime dropOffTime) {
		if(pickUpTime.isAfter(dropOffTime)) {
			throw new BadRequestException(ErrorMessage.RESERVATION_TIME_INCORRECT_MESSAGE);
		}
		
		ReservationStatus [] status= {ReservationStatus.CANCELLED,ReservationStatus.DONE};
		
		List<Reservation> existReservations = reservationRepository.checkCarStatus(car.getId(), pickUpTime, dropOffTime, status);
		
		return  existReservations.isEmpty();
	}
	
	
	public void updateReservation(Long reservationId, Car car, ReservationUpdateRequest reservationUpdateRequest) {
		Reservation reservation= reservationRepository.findById(reservationId).orElseThrow(()->new 
				ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE, reservationId)));
		
		
		if(reservation.getStatus().equals(ReservationStatus.CANCELLED) || reservation.getStatus().equals(ReservationStatus.DONE)) {
			throw new BadRequestException(ErrorMessage.RESERVATION_STATUS_CANT_CHANGE_MESSAGE);
		}
	
		
		//statusu CANCELLED, DONE yaparken zaman valid mi diye kontrol etmesin
		if(reservationUpdateRequest.getStatus()!=null &&reservationUpdateRequest.getStatus()==ReservationStatus.CREATED) {

			boolean carIsAvailable=true;

			checkReservationTimeIsCorrect(reservationUpdateRequest.getPickUpTime(), reservationUpdateRequest.getDropOffTime() );
			carIsAvailable=checkCarAvailability(car, reservationUpdateRequest.getPickUpTime(), reservationUpdateRequest.getDropOffTime());
			
			if(reservationUpdateRequest.getPickUpTime().compareTo(reservation.getPickUpTime())==0 &&
					reservationUpdateRequest.getDropOffTime().compareTo(reservation.getDropOffTime())==0 &&
					car.getId().equals(reservation.getCar().getId())){
				reservation.setStatus(reservationUpdateRequest.getStatus());
			}else if(!carIsAvailable) {
				throw new BadRequestException(ErrorMessage.CAR_NOT_AVAILABLE_MESSAGE);
			}
			
			
			Double totalPrice = getTotalPrice(car, reservationUpdateRequest.getPickUpTime(), reservationUpdateRequest.getDropOffTime());
			
			reservation.setTotalPrice(totalPrice);
			
			reservation.setCar(car);
			
		}
		
		reservation.setPickUpTime(reservationUpdateRequest.getPickUpTime());
		reservation.setDropOffTime(reservationUpdateRequest.getDropOffTime());
		reservation.setPickUpLocation(reservationUpdateRequest.getPickUpLocation());
		reservation.setDropOffLocation(reservationUpdateRequest.getDropOffLocation());
		reservation.setStatus(reservationUpdateRequest.getStatus());
		reservationRepository.save(reservation);
		
	}
	
	
	public void createReservation(ReservationRequest reservationRequest, User user, Car car) {
		
		checkReservationTimeIsCorrect(reservationRequest.getPickUpTime(), reservationRequest.getDropOffTime());
		
		boolean carStatus=checkCarAvailability(car,reservationRequest.getPickUpTime(), reservationRequest.getDropOffTime());
		
		Reservation reservation= reservationMapper.reservationRequestToReservation(reservationRequest);
		
		if(carStatus) {
			reservation.setStatus(ReservationStatus.CREATED);
		}else {
			throw new BadRequestException(ErrorMessage.CAR_NOT_AVAILABLE_MESSAGE);
		}
		
		reservation.setCar(car);
		reservation.setUser(user);
		
		Double totalPrice=getTotalPrice(car, reservationRequest.getPickUpTime(), reservationRequest.getDropOffTime());
     
		reservation.setTotalPrice(totalPrice);
		reservationRepository.save(reservation);
		
	}
	
	public Double getTotalPrice(Car car, LocalDateTime pickUpTime,LocalDateTime dropOffTime) {
		 Long minutes=ChronoUnit.MINUTES.between(pickUpTime,dropOffTime);
		 double hours=Math.ceil(minutes/60.0);
		 return car.getPricePerHour()*hours;
	}
	
}
