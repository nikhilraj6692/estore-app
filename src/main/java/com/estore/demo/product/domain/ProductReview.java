package com.estore.demo.product.domain;

import org.springframework.data.mongodb.core.mapping.Document;

/*
POJO to hold product reviews by users and their ratings
 */
@Document(collection = "ProductReview")
public class ProductReview {
    private String productId;

    private String title;

    private String comments;

    private Rating rating;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
