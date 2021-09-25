package com.estore.demo.order.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/*
POJO object to hold UPI related information
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UPI extends PaymentDetail {
    private String upiAddress;

    private UPIChannel upiChannel;

    private String vericationTokenizedUrl;

    public String getUpiAddress() {
        return upiAddress;
    }

    public void setUpiAddress(String upiAddress) {
        this.upiAddress = upiAddress;
    }

    public UPIChannel getUpiChannel() {
        return upiChannel;
    }

    public void setUpiChannel(UPIChannel upiChannel) {
        this.upiChannel = upiChannel;
    }

    public String getVericationTokenizedUrl() {
        return vericationTokenizedUrl;
    }

    public void setVericationTokenizedUrl(String vericationTokenizedUrl) {
        this.vericationTokenizedUrl = vericationTokenizedUrl;
    }
}
