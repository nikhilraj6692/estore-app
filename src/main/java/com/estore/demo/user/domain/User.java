package com.estore.demo.user.domain;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/*
POJO class to contain buyer and seller info
 */
@Document(collection = "User")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Seller.class, name = "seller"),
        @JsonSubTypes.Type(value = Buyer.class, name = "buyer")
})
public class User {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private List<UserAddress> addressList;

    private List<UserContact> contactsList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<UserAddress> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<UserAddress> addressList) {
        this.addressList = addressList;
    }

    public List<UserContact> getContactsList() {
        return contactsList;
    }

    public void setContactsList(List<UserContact> contactsList) {
        this.contactsList = contactsList;
    }


}