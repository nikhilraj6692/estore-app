package com.estore.demo.order.domain;

/*
Enum to hold status of payment. Asynchronous listeners will update status from one status to another. Implementation for
remaining two status in PROGRESS!!!
 */
public enum PaymentStatus {
    AWAITING_VERIFICATION, PROCESSED, DECLINED
}
