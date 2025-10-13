package com.jeyix.school_jeyix.features.notifications.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnnouncementEvent implements NotifiableEvent {
    private String subject;
    private String message;
    private List<String> targetRoles;

    @Override
    public String getChannelKey() {
        return "PUSH";
    }

    @Override
    public Long getUserId() {
        return null;
    }

    @Override
    public String getRecipient() {
        return null;
    }

    @Override
    public String getActionUrl() {
        return null;
    }
}