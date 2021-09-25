package com.estore.demo.user.domain;

import java.math.BigInteger;

/*
POJO class to contain user contacts
 */
public class UserContact {
    private ContactType contactType;

    private BigInteger phoneNumber;

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public BigInteger getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(BigInteger phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
