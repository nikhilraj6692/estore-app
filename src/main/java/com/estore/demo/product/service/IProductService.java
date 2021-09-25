package com.estore.demo.product.service;

import com.estore.demo.product.domain.ProductMetaData;
import com.estore.demo.product.vo.ProductVO;

import java.util.List;

public interface IProductService {
    String addProduct(ProductVO productVO);

    List<ProductMetaData> fetchProducts(String name, String productName);
}
