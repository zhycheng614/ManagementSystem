package com.backend.management.repository;

import com.backend.management.model.Reservation;
import com.backend.management.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query(value = "SELECT res FROM Reservation res WHERE res.user_id = ?1")
    List<Reservation> findByRequester(String requester_id);

    @Query(value = "SELECT res FROM Reservation res WHERE   res.reservation_id = ?1 AND res.user_id = ?2 ")
    Reservation findByReservationAndRequester(int reservation_id, String requester_id);

    //Reservation findByReservation_idAndRequester_id(int reservation_id, User requester_id); // for deletion
    @Modifying
    @Query(value = "delete from Reservation res where res.reservation_id =?1")
    int deleteByReservation_id(Integer reservation_id);
}

