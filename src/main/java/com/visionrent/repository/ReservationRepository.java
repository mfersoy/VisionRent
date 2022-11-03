package com.visionrent.repository;

import com.visionrent.domain.Reservation;
import com.visionrent.domain.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	
	@EntityGraph(attributePaths = {"car","car.image","user"})
	Optional<Reservation> findById(Long id);
	
	
	@EntityGraph(attributePaths = {"car","car.image"})
	List<Reservation> findAll();
	
	@EntityGraph(attributePaths = {"car","car.image"})
	Page<Reservation> findAll(Pageable pageable);
	

	@Query("SELECT r FROM Reservation r "
			+ "JOIN FETCH Car c on r.car=c.id WHERE "
			+ "c.id=:carId and (r.status not in :status) and :pickUpTime BETWEEN r.pickUpTime and r.dropOffTime "
			+ "or "
			+ "c.id=:carId and (r.status not in :status) and :dropOffTime BETWEEN r.pickUpTime and r.dropOffTime "
			+ "or "
			+ "c.id=:carId and (r.status not in :status) and (r.pickUpTime BETWEEN :pickUpTime and :dropOffTime)")
	List<Reservation> checkCarStatus(@Param("carId") Long carId, @Param("pickUpTime") LocalDateTime pickUpTime,
			@Param("dropOffTime") LocalDateTime dropOffTime,@Param("status") ReservationStatus[] status);
}
