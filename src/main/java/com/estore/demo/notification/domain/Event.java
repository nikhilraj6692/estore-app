package com.estore.demo.notification.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/*
POJO to wrap mailgun webhook response
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

    private String event;

    @JsonProperty("user-variables")
    private Map<String, String> userVariables;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Map<String, String> getUserVariables() {
        return userVariables;
    }

    public void setUserVariables(Map<String, String> userVariables) {
        this.userVariables = userVariables;
    }

}
