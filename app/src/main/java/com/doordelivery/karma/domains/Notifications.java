package com.doordelivery.karma.domains;

public class Notifications {
    String Message,Title,NotificationImage;

    public Notifications() {
    }

    public Notifications(String message, String title, String notificationImage) {
        Message = message;
        Title = title;
        NotificationImage = notificationImage;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getNotificationImage() {
        return NotificationImage;
    }

    public void setNotificationImage(String notificationImage) {
        NotificationImage = notificationImage;
    }
}
