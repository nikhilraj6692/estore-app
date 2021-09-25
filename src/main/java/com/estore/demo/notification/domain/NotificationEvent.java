package com.estore.demo.notification.domain;

import com.estore.demo.notification.service.INotificationChannel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
DTO class for NotificationAlertVO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationEvent {
    Map<String, Object> data;
    RecipientContact recipientContact;
    private String categoryId;
    private String userId;
    private Timestamp time;
    private EventName eventName;
    private List<INotificationChannel> notificationChannels;

    private String templateName;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public RecipientContact getRecipientContact() {
        return recipientContact;
    }

    public void setRecipientContact(RecipientContact recipientContact) {
        this.recipientContact = recipientContact;
    }

    public EventName getEventName() {
        return eventName;
    }

    public void setEventName(EventName eventName) {
        this.eventName = eventName;
    }

    public List<INotificationChannel> getNotificationChannels() {
        if (null == notificationChannels) {
            return new ArrayList<>();
        }
        return notificationChannels;
    }

    public void setNotificationChannels(List<INotificationChannel> notificationChannels) {
        this.notificationChannels = notificationChannels;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Override
    public String toString() {
        return "NotificationEvent{" +
                "categoryId='" + categoryId + '\'' +
                ", userId='" + userId + '\'' +
                ", time=" + time +
                ", data=" + data +
                ", eventName=" + eventName +
                ", recipientContact=" + recipientContact +
                '}';
    }
}
