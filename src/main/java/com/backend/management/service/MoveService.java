package com.backend.management.service;

import com.backend.management.exception.ApartmentNotExistException;
import com.backend.management.exception.MoveException;
import com.backend.management.model.Apartment;
import com.backend.management.model.User;
import com.backend.management.repository.ApartmentRepository;
import com.backend.management.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MoveService {
    private UserRepository userRepository;
    private ApartmentRepository apartmentRepository;

    public MoveService(UserRepository userRepository, ApartmentRepository apartmentRepository) {
        this.userRepository = userRepository;
        this.apartmentRepository = apartmentRepository;
    }

    /** For when newly moving in, or moving from an apartment as sole occupant. */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void moveIn(String username, String apartmentNumber) {
        move(username, apartmentNumber, null);
    }

    /** For when moving from an old apartment to a new one, and old ownership must change. */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void moveInAndAssignNewOwner(String username, String apartmentNumber, String newOwner) {
        move(username, apartmentNumber, newOwner);
    }

    /** When moving out as sole occupant of an apartment. */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void moveOut(String username) {
        move(username, null, null);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Set<String> getFlatmates(String username) {
        Optional<User> userOptional = userRepository.findById(username);
        if (userOptional.isEmpty()) {
            // TODO: handle this error.
            throw new MoveException("Invalid Username!");
        }
        User user = userOptional.get();

        Set<User> occupants = new HashSet<>(userRepository.getOccupants(user.getApartmentNumber().getApartmentId()));
        occupants.remove(user);
        return occupants.stream().map(User::getUsername).collect(Collectors.toSet());
    }

    /** When moving out and ownership of the apartment must change. */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void moveOutAndAssignNewOwner(String username, String newOwner) {
        move(username, null, newOwner);
    }

    public List<String> findApartmentsWithVacancy(){
        List<Apartment> apartments = apartmentRepository.apartmentsWithVacancy();
        return apartments.stream().map(Apartment::getApartmentId).collect(Collectors.toSet()).stream().sorted().collect(Collectors.toList());
    }

    public List<String> getTenantsWithoutApartments() {
        List<User> tenant = userRepository.getTenantsWithoutApartments();
        return tenant.stream().map(User::getUsername).collect(Collectors.toSet()).stream().sorted().collect(Collectors.toList());
    }

    public List<String> getTenantsWithApartments() {
        List<User> tenant = userRepository.getTenantsWithApartments();
        return tenant.stream().map(User::getUsername).collect(Collectors.toSet()).stream().sorted().collect(Collectors.toList());
    }

    //TODO: make a record?
    public static class ApartmentInfo {

        public final String apartmentNumber;
        public final List<String> tenants;

        public final String owner;

        public final String apartmentType;


        public ApartmentInfo(String apartmentNumber, List<String> tenants, String owner, String apartmentType) {
            this.apartmentNumber = apartmentNumber;
            this.tenants = tenants;
            this.owner = owner;
            this.apartmentType = apartmentType;
        }
    }

    public ApartmentInfo getApartmentAndTenantsByApartmentNumber(String apartmentId) {

        List<Object[]> apartmentAndTenants = apartmentRepository.getApartmentAndTenantByApartmentNumber(apartmentId);

        if (apartmentAndTenants.size() == 0) {
            Apartment apartment = apartmentRepository.findById(apartmentId).orElse(null);
            if (apartment == null) {
                throw new ApartmentNotExistException("Apartment does not exist.");
            }
            return new ApartmentInfo(
                    apartment.getApartmentId(),
                    Collections.emptyList(),
                    null,
                    apartment.getApartmentType().getTypeId()
            );
        }

        List<String> tenants = new ArrayList<>();
        Apartment apartment = null;

        for (Object[] objects : apartmentAndTenants) {
            apartment = (Apartment) objects[0];
            User tenant = (User) objects[1];
            tenants.add(tenant.getUsername());
        }

        return new ApartmentInfo(
                apartment.getApartmentId(),
                new HashSet<>(tenants).stream().sorted().collect(Collectors.toList()),
                apartment.getOwnerId().getUsername(),
                apartment.getApartmentType().getTypeId()
        );
    }

    public ApartmentInfo getApartmentAndTenantsByUsername(String username) {

        List<Object[]> apartmentAndTenants = apartmentRepository.getApartmentAndTenantByUsername(username);

        //TODO: simplify this
        if (apartmentAndTenants.size() == 0) {
            User user = userRepository.findById(username).orElse(null);
            if (user == null || user.getApartmentNumber() == null) {
                throw new ApartmentNotExistException("Apartment does not exist.");
            }
            return new ApartmentInfo(
                    user.getApartmentNumber().getApartmentId(),
                    Collections.emptyList(),
                    null,
                    user.getApartmentNumber().getApartmentType().getTypeId()
            );
        }

        List<String> tenants = new ArrayList<>();
        Apartment apartment = null;

        for (Object[] objects : apartmentAndTenants) {
            apartment = (Apartment) objects[0];
            User tenant = (User) objects[1];
            tenants.add(tenant.getUsername());
        }

        return new ApartmentInfo(
                apartment.getApartmentId(),
                new HashSet<>(tenants).stream().sorted().collect(Collectors.toList()),
                apartment.getOwnerId().getUsername(),
                apartment.getApartmentType().getTypeId()
        );
    }

    private void move(String username, String newApartmentNumber, String newOwnerUsername) {
        Optional<User> userOptional = userRepository.findById(username);
        if (userOptional.isEmpty()) {
            // TODO: handle this error.
            throw new MoveException("Invalid Username!");
        }
        User user = userOptional.get();

        Apartment newApartment = null;
        if (newApartmentNumber != null) {
            Optional<Apartment> apartmentOptional = apartmentRepository.findById(newApartmentNumber);
            if (apartmentOptional.isEmpty()) {
                // TODO: handle this error.
                throw new MoveException("Invalid Apartment Number");
            }
            newApartment = apartmentOptional.get();
        }

        User newOwner = null;
        if (newOwnerUsername != null) {
            Optional<User> newOwnerOptional = userRepository.findById(newOwnerUsername);
            if (newOwnerOptional.isEmpty()) {
                // TODO: handle this error.
                throw new MoveException("Invalid Username");
            }
            newOwner = newOwnerOptional.get();
        }

        Apartment oldApartment = null;
        if (user.getApartmentNumber() != null) {
            Optional<Apartment> oldApartmentOptional = apartmentRepository.findById(user.getApartmentNumber().getApartmentId());
            if (oldApartmentOptional.isEmpty()) {
                // TODO: handle this error.
                throw new MoveException("Invalid Apartment Number");
            }
            oldApartment = oldApartmentOptional.get();
        }

        // Check new apartment for occupancy and handle ownership.
        if (newApartment != null) {

            if (oldApartment == newApartment) {
                throw new MoveException("Tenant already lives here");
            }

            int newApartmentOccupantsCount = userRepository.getOccupants(newApartment.getApartmentId()).size();

            int newApartmentVacancy = newApartment.getApartmentType().getCapacity() - newApartmentOccupantsCount;

            if (newApartmentVacancy < 1) {
                throw new MoveException("Apartment is full");
            }

            // Automatically set new single occupant as owner.
            if (newApartmentOccupantsCount == 0) {
                newApartment.setOwnerId(user);
                apartmentRepository.save(newApartment);
            }
        }


        // Handle old apartment ownership.
        if (oldApartment != null && oldApartment.getOwnerId() == user) {

            int oldApartmentOccupantsCount = userRepository.getOccupants(oldApartment.getApartmentId()).size();

            if (oldApartmentOccupantsCount > 1) {
                if (newOwner == null) {
                    throw new MoveException("No new owner submitted");
                }
                oldApartment.setOwnerId(newOwner);
            } else {
                if (newOwner != null) {
                    throw new MoveException("New owner does not occupy this apartment: " + newOwnerUsername);
                }
                oldApartment.setOwnerId(null);
            }
            apartmentRepository.save(oldApartment);
        }

        // Update to new apartment.
        user.setApartmentNumber(newApartment);
        userRepository.save(user);
    }



}
