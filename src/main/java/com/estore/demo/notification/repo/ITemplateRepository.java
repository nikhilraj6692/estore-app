package com.estore.demo.notification.repo;

import com.estore.demo.notification.domain.ChannelType;
import com.estore.demo.notification.domain.EventName;
import com.estore.demo.notification.domain.Template;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/*
Spring JPA repository to query Template collection
 */
@Repository
public interface ITemplateRepository extends MongoRepository<Template, String> {

    public Template findByEventNameAndChannelType(EventName eventName, ChannelType channelType);
}
