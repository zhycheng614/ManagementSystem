package com.backend.management.controller;

import com.backend.management.exception.ReservationNotFoundException;
import com.backend.management.model.Amenity;
import com.backend.management.model.Reservation;
import com.backend.management.model.User;
import com.backend.management.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    //1. get reservation by tenant
    @GetMapping(value = "/reservations")
    public List<Reservation> listByTenant(Principal principal) {
        User username = new User.Builder().setUsername(principal.getName()).build();
        return reservationService.listByUser(username);
    }

    //2. get reservation by manager
    @GetMapping(value = "/reservation")
    public List<Reservation> listByManager(@RequestParam(name = "manager") String managerName) {
        User manager = new User.Builder().setUsername(managerName).build();
        return reservationService.listByUser(manager);
    }

    //3. get reservation by id
    @GetMapping(value = "/reservation/id")
    public Reservation getReservationId(
            @RequestParam(value = "reservation_id") int reservation_id,
            @RequestParam(name = "username") String username
    ) throws ReservationNotFoundException {
        User user = new User.Builder().setUsername(username).build();
        return reservationService.findByIdAndUsername(reservation_id, user);
    }


    //4. add reservation
    @PostMapping("/reservation")
    public void addReservation(@RequestBody Reservation reservation){
        reservation.setDate(LocalDate.now());
        reservationService.add(reservation);
    }

    //5. delete reservation
    @DeleteMapping("/reservation")
    public void deleteReservation(
            @RequestParam(name = "username") String username,
            @RequestParam(value = "reservation_id") Integer reservation_id
    ) throws ReservationNotFoundException {
        reservationService.delete(reservation_id, username);
    }
}

