package com.estore.demo.notification.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
POJO to wrap mailgun webhook response
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MailGunResponse {
    private Signature signature;

    @JsonProperty("event-data")
    private Event eventData;

    public Signature getSignature() {
        return signature;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }

    public Event getEventData() {
        return eventData;
    }

    public void setEventData(Event eventData) {
        this.eventData = eventData;
    }
}
