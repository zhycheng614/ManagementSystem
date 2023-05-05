package com.backend.management.controller;

import com.backend.management.model.Announcement;
import com.backend.management.model.User;
import com.backend.management.service.AnnouncementService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

@RestController
public class AnnouncementController {
    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    //add announcement
    @PostMapping(value = "/announcement")
    public void addAnnouncement(
            @RequestParam("title") String announcementTitle,
            @RequestParam("content") String content,
            @RequestParam("priority") String priority,
            @RequestParam("time") LocalDate time,
            @RequestParam("manager") String managerId) {

        Announcement announcement = new Announcement.Builder()
                .setAnnouncementTitle(announcementTitle)
                .setContent(content)
                .setTime(time)
                .setPriority(priority)
                .setManagerId(new User.Builder().setUsername(managerId).build())
                .build();

        announcementService.add(announcement);
    }

    //detele announcement
    @DeleteMapping(value = "/announcement/{announcement_id}")
    public void deleteAnnouncement(@PathVariable Long announcementId) {
        announcementService.delete(announcementId);
    }

    //get all announcements
    @GetMapping(value = "/announcements")
    public void getAnnouncements() {
        announcementService.listByAnnouncements();
    }

//    //get announcement by username
//    @GetMapping(value = "/announcement/{manager}")
//    public void getAnnouncementByUsername(@PathVariable String manager) {
//        announcementService.listByManager(manager);
//    }
}