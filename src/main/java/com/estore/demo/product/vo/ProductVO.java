package com.estore.demo.product.vo;

import com.estore.demo.product.domain.ProductMetaData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/*
POJO class to hold product info posted by seller
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductVO {

    private String id;

    @NotNull
    private Double price;

    @Min(1)
    private int quantity;

    @Valid
    private ProductMetaData metaData;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ProductMetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(ProductMetaData metaData) {
        this.metaData = metaData;
    }
}