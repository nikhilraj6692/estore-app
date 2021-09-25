package com.estore.demo.product.repo;

import com.estore.demo.product.domain.ProductMetaData;

import java.util.List;

public interface IProductMetaDataRepo {

    List<ProductMetaData> findAllProductMetaDataByNameAndCategory(String productName, List<String> category);
}
