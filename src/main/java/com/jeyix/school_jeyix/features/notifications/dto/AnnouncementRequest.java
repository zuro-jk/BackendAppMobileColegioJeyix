package com.jeyix.school_jeyix.features.notifications.dto;

import java.util.List;

import lombok.Data;

@Data
public class AnnouncementRequest {
    private String title;
    private String body;
    private List<String> targetRoles;
}
