package com.estore.demo.order.repo;

import com.estore.demo.order.domain.PaymentDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/*
Spring JPA repository to query PaymentDetails collection
 */
@Repository
public interface IPaymentRepository extends MongoRepository<PaymentDetail, String> {

}
