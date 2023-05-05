package com.backend.management.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "announcements")
@JsonDeserialize(builder = Announcement.Builder.class)
public class Announcement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Long announcementId;

    @ManyToOne
    @JoinColumn(name = "manager")
    private User managerId;

    @JsonProperty("title")
    private String announcementTitle;
    private String content;
    private LocalDate time;
    private String priority;


    public Announcement() {}

    private Announcement(Builder builder) {
        this.announcementId = builder.announcementId;
        this.managerId = builder.managerId;
        this.announcementTitle = builder.announcementTitle;;
        this.content = builder.content;
        this.time = builder.time;
        this.priority = builder.priority;
    }

    public Long getAnnouncementId() {
        return announcementId;
    }

    public User getManagerId() {
        return managerId;
    }

    public String getAnnouncementTitle() {
        return announcementTitle;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getTime() {
        return time;
    }

    public String getPriority() {
        return priority;
    }

    public static class Builder {

        @JsonProperty("id")
        private Long announcementId;

        @JsonProperty("manager")
        private User managerId;

        @JsonProperty("title")
        private String announcementTitle;

        @JsonProperty("content")
        private String content;

        @JsonProperty("time")
        private LocalDate time;

        @JsonProperty("priority")
        private String priority;

        public Builder setAnnouncementId(Long announcementId) {
            this.announcementId = announcementId;
            return this;
        }

        public Builder setManagerId(User managerId) {
            this.managerId = managerId;
            return this;
        }

        public Builder setAnnouncementTitle(String announcementTitle) {
            this.announcementTitle = announcementTitle;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setTime(LocalDate time) {
            this.time = time;
            return this;
        }

        public Builder setPriority(String priority) {
            this.priority = priority;
            return this;
        }

        public Announcement build() {
            return new Announcement(this);
        }
    }

}
