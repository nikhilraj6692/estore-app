package com.estore.demo.product.rest;

import com.estore.demo.common.aspectj.RequiredPermission;
import com.estore.demo.common.domain.StatusVO;
import com.estore.demo.constants.ApplicationConstants;
import com.estore.demo.product.domain.ProductMetaData;
import com.estore.demo.product.service.IProductService;
import com.estore.demo.product.vo.ProductVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/*
Rest resource for product related use cases
 */
@RestController
@RequestMapping(path = "/v1/products")
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    @Autowired
    private IProductService productService;

    @Autowired
    private MessageSource messageSource;

    /*
    Rest resource to add/update product by seller
     */
    @PutMapping(path = "/addproduct", consumes = "application/json", produces = "application/json")
    @RequiredPermission(capabilityId = "Product.AddProduct", permission = RequiredPermission.PermissionValue.UPDATE)
    public ResponseEntity<StatusVO> addProduct(@Valid @RequestBody ProductVO productVO) throws Exception {
        log.debug("Create product starts");
        String productId = productService.addProduct(productVO);

        StatusVO statusVO = new StatusVO();
        statusVO.setStatus(HttpStatus.CREATED);
        statusVO.setMessage(messageSource.getMessage
                (ApplicationConstants.PRODUCT_CREATED_SUCCESSFULLY, null, Locale.getDefault()));
        Map<String, String> map = new HashMap<>();
        map.put("productId", productId);
        statusVO.setDetails(map);
        return ResponseEntity.status(201).body(statusVO);
    }

    /*
    Rest resource to fetch products efficiently with category and/or regex supported product names
     */
    @GetMapping(path = "/getproducts-by-name-or-category", produces = "application/json")
    @RequiredPermission(capabilityId = "Dashboard.SearchProduct", permission = RequiredPermission.PermissionValue.READ)
    public List<ProductMetaData> getProducts(@RequestParam(name = "name", required = false) String productName
            , @RequestParam(name = "category", required = false) String category) {
        log.debug("Fetch product starts");
        List<ProductMetaData> metadataList = productService.fetchProducts(productName, category);

        if (metadataList.isEmpty())
            return Arrays.asList();

        return metadataList;
    }
}
