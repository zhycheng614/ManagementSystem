package com.backend.management.controller;

import com.backend.management.exception.ReservationNotFoundException;
import com.backend.management.model.Reservation;
import com.backend.management.model.User;
import com.backend.management.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @GetMapping(value = "/reservationfindanewnamebeforereleasing")
    public List<Reservation> listByTenant(@RequestParam(name = "tenant") String tenantName) {
        User tenant = new User.Builder().setUsername(tenantName).build();
        return reservationService.listByUser(tenant);
    }

    @GetMapping(value = "/reservation")
    public List<Reservation> listByManager(@RequestParam(name = "manager") String managerName) {
        User manager = new User.Builder().setUsername(managerName).build();
        return reservationService.listByUser(manager);
    }

    @GetMapping(value = "/reservation/id")
    public Reservation getReservationId(
            @RequestParam(value = "reservation_id") int reservation_id,
            @RequestParam(name = "username") String username
    ) throws ReservationNotFoundException {
        User user = new User.Builder().setUsername(username).build();
        return reservationService.findByIdAndUsername(reservation_id, user);
    }

    @PostMapping("/reservation")
    public void addReservation(@RequestBody Reservation reservation){
        reservationService.add(reservation);
    }

    @DeleteMapping("/reservation")
    public void deleteReservation(
            @RequestParam(name = "username") String username,
            @RequestParam(value = "reservation_id") int reservation_id
    ) throws ReservationNotFoundException {
        reservationService.delete(reservation_id, username);
    }
}

