package com.backend.management.service;

import com.backend.management.model.Announcement;
import com.backend.management.model.User;
import com.backend.management.repository.AnnouncementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }


    //add announcement
    public void add(Announcement announcement) {
        announcementRepository.save(announcement);
    }

    //detele announcement
    public void delete(Long announcementId) {
        announcementRepository.deleteById(announcementId);
    }

    //get all announcements
    public List<Announcement> listByAnnouncements() {
        return announcementRepository.findAll();
    }

////    //get announcement by username
//    public List<Announcement> listByManager(String username) {
//        return announcementRepository.findByManager(new User.Builder().setUsername(username).build());
//    }


}
