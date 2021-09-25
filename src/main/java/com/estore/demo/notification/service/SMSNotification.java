package com.estore.demo.notification.service;

import com.estore.demo.notification.domain.ChannelType;
import com.estore.demo.notification.domain.Notification;
import com.estore.demo.notification.domain.NotificationEvent;
import com.estore.demo.notification.repo.ITemplateRepository;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
Service class to implement sms related notifications
 */
@Component("smsNotification")
public class SMSNotification extends INotificationChannel{

    @Autowired
    public SMSNotification(ITemplateRepository templateRepository, VelocityEngine velocityEngine) {
        super(templateRepository, velocityEngine);
        super.channelType = ChannelType.TEXT;
    }

    /*
    Implementation in PROGRESS!!!
     */
    @Override
    public Notification processNotification(NotificationEvent notificationEvent) {
        //send notification via
        return null;
    }
}
