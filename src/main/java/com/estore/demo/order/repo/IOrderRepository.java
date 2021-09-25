package com.estore.demo.order.repo;

import com.estore.demo.order.domain.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/*
Spring JPA repository to query Order collection
 */
@Repository
public interface IOrderRepository extends MongoRepository<Order, String> {
}
