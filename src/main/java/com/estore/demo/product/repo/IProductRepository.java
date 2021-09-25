package com.estore.demo.product.repo;

import com.estore.demo.product.domain.Product;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
Spring JPA repository to query Product collection
 */
@Repository
public interface IProductRepository extends PagingAndSortingRepository<Product, String> {

    List<Product> findByIdIn(List<String> productIds);
}
