package com.estore.demo.product.repo;

import com.estore.demo.product.domain.ProductCategory;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
Spring JPA repository to query Product Category collection
 */
@Repository
public interface IProductCategoryRepository extends PagingAndSortingRepository<ProductCategory, String> {

    ProductCategory findByName(String productCategoryName);

    List<ProductCategory> findByNameIgnoreCaseOrParentIgnoreCaseOrAncestorsIgnoreCase(String name, String parent, String ancestor);
}
