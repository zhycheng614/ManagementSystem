package com.backend.management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "reservation")
@JsonDeserialize(builder = Reservation.Builder.class)
public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int reservation_id;
    @JsonProperty("reservationName")
    private String reservation_name;
    @ManyToOne
    @JoinColumn(name = "amenity_id")
    private Amenity amenity_id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User requester_id;

    @JsonProperty("guestCount")
    private int guest_count;
    private LocalDate date;
    @JsonProperty("startTime")
    private int start_time;
    @JsonProperty("endTime")
    private int end_time;

    public Reservation() {}

    private Reservation(Builder builder){
        this.reservation_id = builder.reservation_id;
        this.reservation_name = builder.reservation_name;
        this.amenity_id = builder.amenity_id;
        this.requester_id = builder.requester_id;
        this.guest_count = builder.guest_count;
        this.date = builder.date;
        this.start_time = builder.start_time;
        this.end_time = builder.end_time;
    }

    public int getReservationID(){
        return reservation_id;
    }
    public String getReservationName(){
        return reservation_name;
    }
    public Amenity getAmenityID(){
        return amenity_id;
    }
//     public User setUser(User username){
//        this.username = username;
//        return this;
//    }

    public int getGuestCount(){
        return guest_count;
    }
    public LocalDate getDate(){
        return date;
    }
    public int getStartTime(){
        return start_time;
    }
    public int getEndTime(){
        return end_time;
    }


    public static class Builder{
        @JsonProperty("reservationId")
        private int reservation_id;
        @JsonProperty("reservationName")
        private String reservation_name;
        @JsonProperty("amenityId")
        private Amenity amenity_id;
        @JsonProperty("requesterId")
        private User requester_id;
        @JsonProperty("guestCount")
        private int guest_count;
        @JsonProperty("date")
        private LocalDate date;
        @JsonProperty("startTime")
        private int start_time;
        @JsonProperty("endTime")
        private int end_time;
        private Amenity amenity;

        public Builder setReservationName(int reservation_id) {
            this.reservation_id = reservation_id;
            return this;
        }
        public Builder setReservationName(String reservation_name){
            this.reservation_name = reservation_name;
            return this;
        }
        public Builder setRequesterID(User requester_id){
            this.requester_id = requester_id;
            return this;
        }

        public Builder setAmenityID(Amenity amenity_id){
            this.amenity_id = amenity_id;
            return this;
        }
        public Builder setGuestCount(int guest_count){
            this.guest_count = guest_count;
            return this;
        }
        public Builder setDate(LocalDate date){
            this.date = date;
            return this;
        }
        public Builder setStartTime(int start_time){
            this.start_time = start_time;
            return this;
        }
        public Builder setEndTime(int end_time){
            this.end_time = end_time;
            return this;
        }
        public Reservation build(){
            return new Reservation(this);
        }
    }

}
