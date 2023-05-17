package com.backend.management.service;

import com.backend.management.exception.ReservationCollisionException;
import com.backend.management.exception.ReservationNotFoundException;
import com.backend.management.model.Reservation;
import com.backend.management.model.User;
import com.backend.management.model.vo.ReservationVo;

import com.backend.management.repository.ReservationRepository;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Map<Integer, List<ReservationVo>> listByToday(User username) {
        Map<Integer, List<ReservationVo>> hashMap = new HashMap<>();
        List<ReservationVo> voList = new ArrayList<>();
        List<Reservation> byRequester = reservationRepository.findByRequester(username.getUsername());
        for (Reservation reservation : byRequester) {
            ReservationVo reservationVo = new ReservationVo();
            reservationVo.setReservation_id(reservation.getReservationID());
            reservationVo.setStartTime(reservation.getStartTime());
            reservationVo.setEndTime(reservation.getEndTime());
            String date = reservation.getDate();
//            大于date
            LocalDate now = LocalDate.now();
            LocalDate parse = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (parse.isAfter(now)) {
                reservationVo.setDate(date);
                voList.add(reservationVo);
            }
            reservationVo.setAmenityId(Integer.parseInt(reservation.getAmenity_id()));
        }
//        按照amenityId分组
        for (ReservationVo reservationVo : voList) {
            Integer amenityId = reservationVo.getAmenityId();
            if (hashMap.containsKey(amenityId)) {
                List<ReservationVo> reservationVos = hashMap.get(amenityId);
                reservationVos.add(reservationVo);
                hashMap.put(amenityId, reservationVos);
            } else {
                List<ReservationVo> reservationVos = new ArrayList<>();
                reservationVos.add(reservationVo);
                hashMap.put(amenityId, reservationVos);
            }
        }
        return hashMap;
    }

    //1.get reservation by id
    public Reservation findByIdAndUsername(int reservation_id, User username) throws ReservationNotFoundException {
        Reservation reservation = reservationRepository.findByReservationAndRequester(reservation_id, username.getUsername());
        if (reservation == null) {
            throw new ReservationNotFoundException("Reservation not found");
        }
        return reservation;
    }

    //2.Add reservation
    @Transactional
    public void add(List<Reservation> reservation) throws ReservationCollisionException {
        for (Reservation reservation1 : reservation) {
            Reservation reservation2 = reservationRepository.
                    findReservationByDateAndAmenityStarttimeAndEndTime(
                            reservation1.getDate(), reservation1.getStartTime(), reservation1.getEndTime(), reservation1.getAmenity_id());
            if (reservation2 != null) {
                throw new ReservationCollisionException("Reservation collision");
            }
            reservationRepository.save(reservation1);
        }
    }

    //3.delete reservation by id
    @Transactional //(Isolation = Isolation.SERIALIZABLE)
    public void delete(Integer reservation_id, String username) throws ReservationNotFoundException {
//        User user = new User.Builder().setUsername(username).build();
//        Reservation reservation = reservationRepository.findByReservationAndRequester(reservation_id, user); //username?

        reservationRepository.deleteByReservation_id(reservation_id);
    }

    public List<ReservationVo> listByAll(User username) {
        List<ReservationVo> voList = new ArrayList<>();
        List<Reservation> byRequester = reservationRepository.findByRequester(username.getUsername());
        for (Reservation reservation : byRequester) {
            ReservationVo reservationVo = new ReservationVo();
            reservationVo.setReservation_id(reservation.getReservationID());
            reservationVo.setStartTime(reservation.getStartTime());
            reservationVo.setEndTime(reservation.getEndTime());
//            date转为 yyyy-MM-dee
            reservationVo.setDate(reservation.getDate());
            voList.add(reservationVo);
        }
        return voList;
    }
}