package com.estore.demo.common.config;

import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.net.UnknownHostException;

/*
Configuration file to create mongo instance
 */
@Configuration
public class MongoDBConfig {
    @Value("${mongo.connection.url}")
    private String connectionUrl;

    /*
    Creates MongoDB factory with mongo db url configured in application.properties
     */
    @Bean
    public MongoDbFactory mongoDbFactory() throws UnknownHostException {
        MongoClientURI uri = new MongoClientURI(connectionUrl);
        MongoDbFactory factory = new SimpleMongoDbFactory(uri);
        return factory;
    }

    /*
    Mongo template bean to be used to query mongo database
     */
    @Bean
    public MongoTemplate mongoTemplate() throws UnknownHostException {
        MongoTemplate template = new MongoTemplate(mongoDbFactory());
        return template;
    }
} 