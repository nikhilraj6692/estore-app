package com.estore.demo.notification.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

/*
Notification POJO to save notification metadata in database for audit purpose
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "Notification")
public class Notification {

    @Id
    private String id;

    @JsonProperty("notificationid")
    private String notificationId;

    @JsonProperty("notificationtype")
    private String notificationType;

    @JsonProperty("notificationname")
    private String notificationName;

    @JsonProperty("entityid")
    private String entityId;

    @JsonProperty("entitytype")
    private String entityType;

    @JsonProperty("userid")
    private String userId;

    @JsonProperty("notificationdelivereddate")
    private Date notificationDeliveredDate;

    @JsonProperty("notificationFailureDate")
    private Date notificationFailureDate;

    @JsonProperty("notificationsentdate")
    private Date notificationSentDate;

    @JsonProperty("status")
    private String status;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("notificationtemplate")
    private String notificationTemplate;

    @JsonProperty("details")
    private Map<String, Object> details;

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getNotificationName() {
        return notificationName;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getNotificationDeliveredDate() {
        return notificationDeliveredDate;
    }

    public void setNotificationDeliveredDate(Date notificationDeliveredDate) {
        this.notificationDeliveredDate = notificationDeliveredDate;
    }

    public Date getNotificationFailureDate() {
        return notificationFailureDate;
    }

    public void setNotificationFailureDate(Date notificationFailureDate) {
        this.notificationFailureDate = notificationFailureDate;
    }

    public Date getNotificationSentDate() {
        return notificationSentDate;
    }

    public void setNotificationSentDate(Date notificationSentDate) {
        this.notificationSentDate = notificationSentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNotificationTemplate() {
        return notificationTemplate;
    }

    public void setNotificationTemplate(String notificationTemplate) {
        this.notificationTemplate = notificationTemplate;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId='" + notificationId + '\'' +
                ", notificationType='" + notificationType + '\'' +
                ", notificationName='" + notificationName + '\'' +
                ", entityId='" + entityId + '\'' +
                ", entityType='" + entityType + '\'' +
                ", userId='" + userId + '\'' +
                ", notificationDeliveredDate=" + notificationDeliveredDate +
                ", notificationFailureDate=" + notificationFailureDate +
                ", notificationSentDate=" + notificationSentDate +
                ", status='" + status + '\'' +
                ", reason='" + reason + '\'' +
                ", notificationTemplate='" + notificationTemplate + '\'' +
                ", details=" + details +
                '}';
    }
}
