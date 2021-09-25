package com.estore.demo.notification.repo;

import com.estore.demo.notification.domain.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/*
Spring JPA repository to query Notification collection
 */
@Repository
public interface INotificationRepository extends MongoRepository<Notification, String> {
    Notification findByNotificationId(String notificationId);
}
