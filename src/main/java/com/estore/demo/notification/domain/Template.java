package com.estore.demo.notification.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/*
POJO class to hold dynamic velocity provider templates which will be used to send notification
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "Template")
public class Template {
    @Id
    private String id;

    private EventName eventName;
    private String fileName;
    private ChannelType channelType;

    public Template(EventName eventName, String fileName, ChannelType channelType) {
        this.eventName = eventName;
        this.fileName = fileName;
        this.channelType = channelType;
    }

    public EventName getEventName() {
        return eventName;
    }

    public void setEventName(EventName eventName) {
        this.eventName = eventName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(ChannelType channelType) {
        this.channelType = channelType;
    }
}
