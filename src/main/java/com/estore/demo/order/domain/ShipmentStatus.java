package com.estore.demo.order.domain;

/*
Enum to hold shipment status. Asynchronous listeners will update status from one status to another. Implementation for
remaining two status in PROGRESS!!!
 */
public enum ShipmentStatus {
    PROCESSED, SHIPPED, ORDERED, DELIVERED;
}
