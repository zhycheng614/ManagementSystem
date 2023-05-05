package com.backend.management.repository;

import com.backend.management.model.Announcement;
import com.backend.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
//    List<Announcement> findByManager(User managerId);
    //Announcement findByIdAndHost(Long id, User manager);
}
