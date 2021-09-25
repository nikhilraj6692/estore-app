package com.estore.demo.notification.service;

import com.estore.demo.common.context.LoggedInUser;
import com.estore.demo.constants.ApplicationConstants;
import com.estore.demo.notification.domain.Notification;
import com.estore.demo.notification.domain.NotificationAlertVO;
import com.estore.demo.notification.domain.NotificationEvent;
import com.estore.demo.notification.domain.RecipientContact;
import com.estore.demo.notification.repo.INotificationRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/*
JMS listener to process notifications caught by AMW SQS
 */
@Component
public class MessageListener {

    private final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private LoggedInUser loggedInUser;

    @Autowired
    @Qualifier("emailNotification")
    private INotificationChannel emailNotification;

    @Autowired
    @Qualifier("smsNotification")
    private INotificationChannel smsNotification;

    @Autowired
    private INotificationRepository notificationRepository;

    /*
    Receives messages from AWS queue to process notifications
     */
    @JmsListener(destination = ApplicationConstants.QUEUE)
    public void receiveMessage(NotificationAlertVO notificationAlertVO, @Headers Map<String, Object> headers) {
        /*
        In case the message is failed more than 2 times, then it would be pushed to a dead letter queue configured in SQS which will be dispatching messages to another
        listener(method below). That listener will log status of notification with the same notification id present in the headers. The current listener will log the
        exception each and every time the retry is done and rethrow exception to retry failed message. This will help recover the actual cause of error from the logs
         */
        try {
            NotificationEvent event = buildNotification(notificationAlertVO, (String) headers.get("userId"));
            List<Notification> notifications = new ArrayList<>();

            for (INotificationChannel notificationChannel : event.getNotificationChannels()) {
                notifications.add(notificationChannel.processNotification(event));
            }

            notificationRepository.save(notifications);
        } catch (Exception ex) {
            log.debug("Exception occured for " + headers.get("messageId") + " and exception is : " + ex.getMessage());
            throw new RuntimeException();
        }
    }

    /*
    Fallback listener (receives messages from dead letter queues) to log issues in case primary listener throws exception
     */
    @JmsListener(destination = ApplicationConstants.DEAD_LETTER_QUEUE)
    public void receiveFailedMessage(NotificationAlertVO notificationAlertVO, @Headers Map<String, Object> headers) {
        try {
            Notification notification = new Notification();
            notification.setEntityId(notificationAlertVO.getEntityId());
            notification.setNotificationFailureDate(new Date());

            //here message id would be set in place of notification id, because notification id is for text and email acknowledgement and not for any kind of compile/
            // run time exception
            notification.setNotificationId((String) headers.get("messageId"));
            notification.setReason("Error during processing of message occured. Please check in logs with message Id saved in database");
            log.debug("Failed Message " + headers.get("messageId") + "and payload as : " + notificationAlertVO);

            notificationRepository.save(notification);

        } catch (Exception ex) {
            //fallback if anything happens to statements in try catch...should not happen
            log.debug("Fallback for " + headers.get("messageId") + " and exception is : " + ex.getMessage());
        }
    }

    /*
    VO to DTO conversion
     */
    private NotificationEvent buildNotification(NotificationAlertVO notificationAlertVO, String userId) {
        log.debug("Notification building start");
        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setCategoryId(notificationAlertVO.getEntityId());
        notificationEvent.setUserId(userId);
        notificationEvent.setData(notificationAlertVO.getData());
        LocalDateTime ldt = LocalDateTime.now();
        notificationEvent.setTime(Timestamp.valueOf(ldt));
        notificationEvent.setEventName(notificationAlertVO.getEventName());

        if (StringUtils.isNotBlank(notificationAlertVO.getEmailId()) ||
                StringUtils.isNotEmpty(notificationAlertVO.getPhoneNumber())) {
            RecipientContact recipientContact = new RecipientContact();
            recipientContact.setFirstName((String) notificationAlertVO.getData().get("firstName"));

            if (StringUtils.isNotEmpty(notificationAlertVO.getPhoneNumber())) {
                List<INotificationChannel> notificationChannels = notificationEvent.getNotificationChannels();
                notificationChannels.add(smsNotification);
                notificationEvent.setNotificationChannels(notificationChannels);
            }
            recipientContact.setPhoneNumber(notificationAlertVO.getPhoneNumber());

            if (StringUtils.isNotEmpty(notificationAlertVO.getEmailId())) {
                List<INotificationChannel> notificationChannels = notificationEvent.getNotificationChannels();
                notificationChannels.add(emailNotification);
                notificationEvent.setNotificationChannels(notificationChannels);
            }
            recipientContact.setSubject(notificationAlertVO.getSubject());
            recipientContact.setFrom(ApplicationConstants.DEFAULT_EMAIL_ID);
            List<String> to = new ArrayList<>();
            to.add(notificationAlertVO.getEmailId());
            recipientContact.setTo(to);

            notificationEvent.setRecipientContact(recipientContact);
        }

        log.debug("NotificationEvent built " + notificationEvent);
        return notificationEvent;
    }
}
