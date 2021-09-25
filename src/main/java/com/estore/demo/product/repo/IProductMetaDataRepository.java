package com.estore.demo.product.repo;

import com.estore.demo.product.domain.ProductMetaData;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/*
Spring JPA repository to query ProductMetaData collection
 */
@Repository
public interface IProductMetaDataRepository extends PagingAndSortingRepository<ProductMetaData, String> {

    ProductMetaData findById(String productId);
}
