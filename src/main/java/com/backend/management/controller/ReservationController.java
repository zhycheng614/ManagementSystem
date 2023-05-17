package com.backend.management.controller;

import com.backend.management.exception.ReservationNotFoundException;
import com.backend.management.model.Amenity;
import com.backend.management.model.Reservation;
import com.backend.management.model.User;
import com.backend.management.model.vo.ReservationVo;
import com.backend.management.service.ReservationService;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    //1. get all reservation by tenant
    @GetMapping(value = "/reservations")
    public List<ReservationVo> listByTenant(Principal principal) {
        User username = new User.Builder().setUsername(principal.getName()).build();
        return reservationService.listByAll(username);
    }

    //2. get reservation by username --> API by only tenant
    @GetMapping(value = "/reservation")
    public List<ReservationVo> listByUserName(@RequestParam(name = "username") String username) {
        User manager = new User.Builder().setUsername(username).build();
        return reservationService.listByAll(manager); //为什么是manager？因为不管是manager还是tenant，其实内容都是一样的，只是分了接口和接口权限。所以不用管他
    }

    //6. get reservation by date --> API for both tenant and manager
    @GetMapping(value = "/reservation/today")
    public Map<Integer,List<ReservationVo>> listByToday(@RequestParam(name = "username") String username) {
        User user = new User.Builder().setUsername(username).build();
        return reservationService.listByToday(user);
    }

    //2. get reservation by id
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
    public void addReservation(@RequestBody List<Reservation> reservation) {
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

