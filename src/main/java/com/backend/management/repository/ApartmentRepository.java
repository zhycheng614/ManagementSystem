package com.backend.management.repository;

import com.backend.management.model.Apartment;
import com.backend.management.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, String> {

    @Query("SELECT a FROM Apartment a LEFT JOIN User u ON u.apartmentNumber = a.apartmentNumber WHERE u.apartmentNumber = NULL")
    List<Apartment> apartmentsWithVacancy();

}

