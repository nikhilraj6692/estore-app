package com.estore.demo.order.vo;

import com.estore.demo.order.domain.OrderItem;
import com.estore.demo.order.domain.PaymentDetail;
import com.estore.demo.user.domain.UserAddress;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.List;

/*
VO object to hold order related info
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderVO {
    @Valid
    @NotEmpty
    private List<OrderItem> orderItems;

    @Valid
    private UserAddress address1;

    @Valid
    private UserAddress address2;

    @Valid
    private PaymentDetail paymentDetail;

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public UserAddress getAddress1() {
        return address1;
    }

    public void setAddress1(UserAddress address1) {
        this.address1 = address1;
    }

    public UserAddress getAddress2() {
        return address2;
    }

    public void setAddress2(UserAddress address2) {
        this.address2 = address2;
    }

    public PaymentDetail getPaymentDetail() {
        return paymentDetail;
    }

    public void setPaymentDetail(PaymentDetail paymentDetail) {
        this.paymentDetail = paymentDetail;
    }
}
