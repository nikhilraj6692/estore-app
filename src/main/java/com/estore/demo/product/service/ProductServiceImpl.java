package com.estore.demo.product.service;

import com.estore.demo.common.context.LoggedInUser;
import com.estore.demo.common.exceptions.BusinessException;
import com.estore.demo.constants.ApplicationConstants;
import com.estore.demo.notification.domain.EventName;
import com.estore.demo.notification.domain.NotificationAlertVO;
import com.estore.demo.notification.service.INotificationService;
import com.estore.demo.product.domain.Product;
import com.estore.demo.product.domain.ProductCategory;
import com.estore.demo.product.domain.ProductMetaData;
import com.estore.demo.product.repo.IProductCategoryRepository;
import com.estore.demo.product.repo.IProductMetaDataRepo;
import com.estore.demo.product.repo.IProductMetaDataRepository;
import com.estore.demo.product.repo.IProductRepository;
import com.estore.demo.product.vo.ProductVO;
import com.estore.demo.user.domain.Seller;
import com.estore.demo.user.repo.IUserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/*
Service layer to implement product related use cases
 */
@Component
public class ProductServiceImpl implements IProductService {
    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private LoggedInUser loggedInUser;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IProductMetaDataRepository productMetaDataRepository;

    @Autowired
    private IProductCategoryRepository productCategoryRepository;

    @Autowired
    private IProductMetaDataRepo productMetaDataRepo;

    @Autowired
    private INotificationService notificationService;

    /*
    Implementation to add or update product by seller
     */
    @Override
    public String addProduct(ProductVO productVO) {
        String userId = loggedInUser.getUserId();

        ProductMetaData metadata = null;
        Product product = null;

        /*
        in case product id is received in the request, it means that product must be available in product metadata and product
        collections. In case, seller leaves the platform or stops selling product 'A', then the product must be removed from
        product collection but would be preserved in product metadata collection so that the ecommerce website may know that
        what was the specification of the product that the seller posted. We assume here that an unique product id would be
        generated against each product irrespective of the seller, in short one product cannot be sold by different sellers
        (just for making it easy)
         */
        if (null != productVO.getId()) {
            metadata = productMetaDataRepository.findById(productVO.getId());
            if (null == metadata) {
                throw new BusinessException(ApplicationConstants.PRODUCT_METADATA_NOT_FOUND, new Object[]{productVO.getId()});
            }

            product = productRepository.findOne(productVO.getId());
            if (null == product) {
                throw new BusinessException(ApplicationConstants.PRODUCT_ID_NOT_FOUND, new Object[]{productVO.getId()});
            }
        }

        String generatedProductId = ApplicationConstants.PRODUCT_PREFIX + RandomStringUtils.randomNumeric(3);

        //validate product category if it exists in main product category collection or not
        if (null != productVO.getMetaData()) {
            String categoryName = productVO.getMetaData().getCategory().getName();
            ProductCategory category = productCategoryRepository.
                    findByName(categoryName);
            if (null == category) {
                throw new BusinessException(ApplicationConstants.PRODUCT_CATEGORY_NOT_FOUND, new Object[]{categoryName});
            }
        }

        if (null == metadata) {
            metadata = new ProductMetaData();
            metadata.setId(generatedProductId);
            metadata.setProductName(productVO.getMetaData().getProductName());
            metadata.setImages(productVO.getMetaData().getImages());
            metadata.setSummary(productVO.getMetaData().getSummary());
            metadata.setProductDetail(productVO.getMetaData().getProductDetail());
            metadata.setCategory(productVO.getMetaData().getCategory());
            metadata.setFeatured(productVO.getMetaData().isFeatured());
        }
        if (null == product) {
            product = new Product();
            product.setId(generatedProductId);
            product.setStock(productVO.getQuantity());
            product.setSellerId(userId);
        } else {
            //check if the same seller id is selling this product or not
            if (!product.getSellerId().equalsIgnoreCase(userId)) {
                throw new BusinessException(ApplicationConstants.INVALID_SELLER_ID_TAGGED_TO_PRODUCT, new Object[]{product.getId()});
            }

            product.setStock(product.getStock() + productVO.getQuantity());
        }
        metadata.setLastUpdatedDate(new Date());
        product.setPrice(productVO.getPrice());

        productRepository.save(product);
        productMetaDataRepository.save(metadata);

        //save posted products in seller account details
        Seller user = userRepository.findById(userId);
        if (user.getPostedProductIds() == null) {
            user.setPostedProductIds(new HashSet<>());
        }
        user.getPostedProductIds().add(product.getId());

        userRepository.save(user);

        /*
        send notification
         */
        NotificationAlertVO alertVO = new NotificationAlertVO();
        alertVO.setEntityId("Post Product");
        //hardcoded email id...it shoudl be taken from profile
        alertVO.setEmailId("rajputnikhil433@gmail.com");
        alertVO.setEventName(EventName.POSTORUPDATEPRODUCT);

        Map<String, Object> data = new HashMap<>();
        data.put("sellerId", userId);
        alertVO.setData(data);

        //asynchronously send notification
        notificationService.sendNotification(alertVO);

        return generatedProductId;
    }

    /*
    Implementation for fetching products on the basis of product names and/or category. Category searches are done with
    ancestors and parents too so that search remains dynamic enough
     */
    @Override
    public List<ProductMetaData> fetchProducts(String productName, String category) {
        List<ProductCategory> productCategoryList;
        List<String> categories = new ArrayList<>();

        String productNamePattern = StringUtils.isNotBlank(productName) ? "^" + productName : null;

        /*
        search in category if category is not blank
         */
        if (StringUtils.isNotBlank(category)) {
            //search for all records in product category table for which
            // the sent category is either a parent or one of the ancestors or name itself
            productCategoryList = productCategoryRepository.
                    findByNameIgnoreCaseOrParentIgnoreCaseOrAncestorsIgnoreCase(category, category, category);

            if (null != productCategoryList && !productCategoryList.isEmpty()) {
                categories = productCategoryList.stream().map(ProductCategory::getName).collect(Collectors.toList());
            }

        }

        //search for the product in all categories found above, if product id is provided otherwise just
        //search with categories. Sort by featured first, then rating, then lastupdateddate. condition check in repo layer.
        return productMetaDataRepo.findAllProductMetaDataByNameAndCategory(productNamePattern, categories);

    }

}
