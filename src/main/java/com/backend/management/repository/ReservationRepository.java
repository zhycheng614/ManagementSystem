package com.backend.management.repository;

import com.backend.management.model.Reservation;
import com.backend.management.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query(value = "SELECT res FROM Reservation res WHERE res.requester_id = :requester_id")
    List<Reservation> findByRequester(User requester_id);

    @Query(value = "SELECT res FROM Reservation res WHERE res.requester_id = :requester_id AND res.reservation_id = :reservation_id")
    Reservation findByReservationAndRequester(int reservation_id, User requester_id);
    //Reservation findByReservation_idAndRequester_id(int reservation_id, User requester_id); // for deletion
}

