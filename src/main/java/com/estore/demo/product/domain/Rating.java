package com.estore.demo.product.domain;

/*
Types of rating that a user can provide to a product
 */
enum Rating {
    ONE("1"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5");

    private int rating;

    Rating(String r) {
        this.rating = Integer.parseInt(r);
    }
}
