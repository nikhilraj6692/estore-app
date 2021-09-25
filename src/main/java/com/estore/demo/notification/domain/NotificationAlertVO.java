package com.estore.demo.notification.domain;

import com.estore.demo.constants.ApplicationConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Map;

/*
POJO to be sent as an actual parameter by individual service classes in order to send notification to the user. The class will
contain important information which is to be used in order to send notification
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationAlertVO {

    @NotBlank(message = ApplicationConstants.NOTIFICATIONNAME_REQUIRED)
    private EventName eventName;

    private String entityId;

    private String emailId;

    private String phoneNumber;

    private String subject;

    private Map<String, Object> data;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public EventName getEventName() {
        return eventName;
    }

    public void setEventName(EventName eventName) {
        this.eventName = eventName;
    }

    @Override
    public String toString() {
        return "NotificationAlertVO{" +
                "eventName=" + eventName +
                ", entityId='" + entityId + '\'' +
                ", emailId='" + emailId + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", subject='" + subject + '\'' +
                ", data=" + data +
                '}';
    }
}
