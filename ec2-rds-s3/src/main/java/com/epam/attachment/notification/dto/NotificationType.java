package com.epam.attachment.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    UPLOAD("File %s was uploaded"),
    DELETE("File %s was deleted");

    private final String subject;
}
