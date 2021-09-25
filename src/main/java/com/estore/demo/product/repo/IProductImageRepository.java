package com.estore.demo.product.repo;

import com.estore.demo.product.domain.ProductImage;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/*
Spring JPA repository to query Product Image collection
 */
@Repository
public interface IProductImageRepository extends PagingAndSortingRepository<ProductImage, String> {

}
