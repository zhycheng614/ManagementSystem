package com.backend.management.service;

import com.backend.management.exception.ReservationCollisionException;
import com.backend.management.exception.ReservationNotFoundException;
import com.backend.management.model.Reservation;
import com.backend.management.model.User;
import com.backend.management.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> listByUser(User username){
        return reservationRepository.findByRequester(username);
    }

    public Reservation findByIdAndUsername(int reservation_id, User username) throws ReservationNotFoundException {
        Reservation reservation = reservationRepository.findByReservationAndRequester(reservation_id, username);
        if(reservation == null){
            throw new ReservationNotFoundException("Reservation not found");
        }
        return reservation;
    }

    @Transactional
    public void add(Reservation reservation) throws ReservationCollisionException {
        reservationRepository.save(reservation);
    }

    @Transactional //(Isolation = Isolation.SERIALIZABLE)
    public void delete(int reservation_id, String username) throws ReservationNotFoundException{
        User user = new User.Builder().setUsername(username).build();
        Reservation reservation = reservationRepository.findByReservationAndRequester(reservation_id, user); //username?
        if(reservation == null){
            throw new ReservationNotFoundException("Reservation not found");
        }
        reservationRepository.delete(reservation);
    }
}
