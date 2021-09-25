package com.estore.demo.order.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/*
POJO object to hold card related info
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Card extends PaymentDetail {
    private String cardNumber;

    private int expiryMonth;

    private int expiryYear;

    private CardType cardType;

    private String verificationTokenizedUrl;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(int expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public int getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(int expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getVerificationTokenizedUrl() {
        return verificationTokenizedUrl;
    }

    public void setVerificationTokenizedUrl(String verificationTokenizedUrl) {
        this.verificationTokenizedUrl = verificationTokenizedUrl;
    }
}
