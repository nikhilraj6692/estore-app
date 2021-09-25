package com.estore.demo.order.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

/*
POJO object to hold shipment related details against an order placed
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ShipmentDetails {
    private ShipmentStatus shipmentStatus;

    private String trackingId;

    private Boolean isDelivered = false;

    private Date deliveredInstant;

    public ShipmentStatus getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public Boolean getDelivered() {
        return isDelivered;
    }

    public void setDelivered(Boolean delivered) {
        isDelivered = delivered;
    }

    public Date getDeliveredInstant() {
        return deliveredInstant;
    }

    public void setDeliveredInstant(Date deliveredInstant) {
        this.deliveredInstant = deliveredInstant;
    }
}

