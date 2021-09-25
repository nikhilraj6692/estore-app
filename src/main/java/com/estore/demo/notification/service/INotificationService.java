package com.estore.demo.notification.service;


import com.estore.demo.notification.domain.NotificationAlertVO;

public interface INotificationService {
    public void sendNotification(NotificationAlertVO notificationAlertVO);
}
