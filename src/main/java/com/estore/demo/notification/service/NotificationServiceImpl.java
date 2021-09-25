package com.estore.demo.notification.service;

import com.estore.demo.notification.domain.NotificationAlertVO;
import com.estore.demo.common.context.LoggedInUser;
import com.estore.demo.constants.ApplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/*
Prepares JMS message and send notification asynchronously
 */
@Component
public class NotificationServiceImpl implements INotificationService {

    @Autowired
    protected JmsTemplate defaultJmsTemplate;

    @Autowired
    private LoggedInUser loggedInUser;

    /*
    Prepares JMS message and send notification asynchronously
    */
    @Override
    public void sendNotification(NotificationAlertVO notificationAlertVO) {
        defaultJmsTemplate.convertAndSend(ApplicationConstants.QUEUE, notificationAlertVO, message -> {
            final String correlationId = loggedInUser.getCorrelationId();
            message.setObjectProperty("userId", loggedInUser.getUserId());
            message.setObjectProperty("messageId", UUID.randomUUID().toString());
            message.setJMSCorrelationID(correlationId);
            return message;
        });
    }
}
