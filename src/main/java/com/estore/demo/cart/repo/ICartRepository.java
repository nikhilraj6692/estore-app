package com.estore.demo.cart.repo;

import com.estore.demo.cart.domain.CartItems;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/*
Spring JPA repository to query CartItems collection
 */
@Repository
public interface ICartRepository extends MongoRepository<CartItems, String> {

    CartItems findById(String id);
}
