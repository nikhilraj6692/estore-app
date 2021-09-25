package com.estore.demo.product.repo;

import com.estore.demo.product.domain.ProductMetaData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/*
MongoTemplate managed DAO implementation class to query for product meta data collection
 */
@Repository
public class ProductMetaDataRepoImpl implements IProductMetaDataRepo {

    @Autowired
    private MongoTemplate mongoTemplate;

    /*
    Queries by category and/or product name alonog with sorting on featured product, then rating and then the last updated date
    of the product posted by the seller
     */
    @Override
    public List<ProductMetaData> findAllProductMetaDataByNameAndCategory(String productName, List<String> categories) {
        Query query = new Query();

        List<Criteria> criteria = new ArrayList<>();

        if (StringUtils.isNotBlank(productName)) {
            criteria.add(Criteria.where("productName").regex(productName, "i"));
        }

        if (!CollectionUtils.isEmpty(categories)) {
            criteria.add(Criteria.where("category.name").in(categories));
        }

        if (criteria.size() > 0) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
        }

        query.with(new Sort(Sort.Direction.DESC, "featured")).
                with(new Sort(Sort.Direction.DESC, "rating")).
                with(new Sort(Sort.Direction.DESC, "lastUpdatedDate")).limit(500);


        List<ProductMetaData> productMetaDataList = mongoTemplate.find(query, ProductMetaData.class);
        return productMetaDataList;
    }
}
