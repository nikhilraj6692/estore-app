package com.estore.demo.notification.service;

import com.estore.demo.constants.ApplicationConstants;
import com.estore.demo.notification.domain.ChannelType;
import com.estore.demo.notification.domain.Notification;
import com.estore.demo.notification.domain.NotificationEvent;
import com.estore.demo.notification.domain.RecipientContact;
import com.estore.demo.notification.repo.ITemplateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.mail.smtp.SMTPTransport;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

/*
Service class to implement email related notifications
 */
@Component("emailNotification")
public class EmailNotification extends INotificationChannel {

    private final Logger log = LoggerFactory.getLogger(EmailNotification.class);

    @Value("${mailgun.host}")
    private String host;

    @Value("${mailgun.user}")
    private String user;

    @Value("${mailgun.pass}")
    private String pass;

    @Autowired
    public EmailNotification(ITemplateRepository templateRepository, VelocityEngine velocityEngine) {
        super(templateRepository, velocityEngine);
        super.channelType = ChannelType.EMAIL;
    }

    /*
    Makes a call to mailgun to send email and save notification once call is successful
     */
    @Override
    public Notification processNotification(NotificationEvent notificationEvent) {
        String message = parseMessage(notificationEvent);

        //process email via mail gun...
        Properties props = System.getProperties();
        props.put("mail.smtps.host", "smtp.mailgun.org");
        props.put("mail.smtps.auth", "true");
        Session session = Session.getInstance(props, null);

        try (SMTPTransport t = (SMTPTransport) session.getTransport("smtps");) {
            Message msg = new MimeMessage(session);

            String notificationID = UUID.randomUUID().toString();
            Map<String, Object> map = new HashMap<>();
            map.put("notificationId", notificationID);
            msg.setHeader("X-Mailgun-Variables", new ObjectMapper().writeValueAsString(map));
            msg.setFrom(new InternetAddress(notificationEvent.getRecipientContact().getFrom()));

            String addresses = notificationEvent.getRecipientContact().getTo().stream().collect(Collectors.joining(","));

            InternetAddress[] addrs = InternetAddress.parse(addresses, false);
            msg.setRecipients(Message.RecipientType.TO, addrs);
            msg.setSubject(notificationEvent.getRecipientContact().getSubject());
            msg.setContent(message, "text/html; charset=utf-8");
            msg.setSentDate(notificationEvent.getTime());

            t.connect(host, user, pass);
            t.sendMessage(msg, msg.getAllRecipients());
            log.info("Response: " + t.getLastServerResponse());

            //save Notification
            return saveNotification(notificationEvent, notificationID);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /*
    prepares and saves notification data in database
     */
    private Notification saveNotification(NotificationEvent notificationEvent, String notificationID) {
        Notification notification = new Notification();
        notification.setNotificationId(notificationID);
        notification.setStatus(ApplicationConstants.PROCESSED);
        notification.setNotificationSentDate(notificationEvent.getTime());
        notification.setNotificationName(notificationEvent.getEventName().toString());
        notification.setNotificationType(ChannelType.EMAIL.toString());
        notification.setEntityId(notificationEvent.getCategoryId());
        notification.setUserId(notificationEvent.getUserId());
        notification.setNotificationTemplate(notificationEvent.getTemplateName());

        //prepare data
        prepareData(notificationEvent.getData(), notificationEvent);

        notification.setDetails(notificationEvent.getData());

        return notification;
    }

    /*
    prepare dynamic user related info to be saved in DB
     */
    private void prepareData(Map<String, Object> data, NotificationEvent notificationEvent) {
        RecipientContact contact = notificationEvent.getRecipientContact();
        data.put("firstName", contact.getFirstName());
        data.put("lastName", contact.getLastName());
        data.put("from", contact.getFrom());
        data.put("subject", contact.getSubject());
        data.put("to", contact.getTo());
    }


}
