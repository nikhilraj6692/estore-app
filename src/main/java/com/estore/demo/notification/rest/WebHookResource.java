package com.estore.demo.notification.rest;

import com.estore.demo.constants.ApplicationConstants;
import com.estore.demo.notification.domain.MailGunResponse;
import com.estore.demo.notification.domain.Notification;
import com.estore.demo.notification.repo.INotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


/*
Resource class to check and update the status of email notifications. It is a call back url which will be called via mailgun.
 */
@RestController
@RequestMapping("/email/callback")
public class WebHookResource {

    private final Logger log = LoggerFactory.getLogger(WebHookResource.class);

    @Value("${mailgun.key}")
    String mailgunKey;

    @Autowired
    private INotificationRepository notificationRepository;

    private String token = null;
    private String timestamp = null;
    private String signature = null;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public void verifyAndUpdateEmailDeliveryStatus(HttpServletRequest requestContext) {
        MailGunResponse response = null;
        try {
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = requestContext.getReader(); //new BufferedReader(new InputStreamReader(inputStream));
            char[] charBuffer = new char[128];
            int bytesRead;
            while ((bytesRead = reader.read(charBuffer)) != -1) {
                builder.append(charBuffer, 0, bytesRead);
            }
            log.info("webhook payload is : " + builder);

            if (builder != null) {
                response = new ObjectMapper().readValue(builder.toString(), MailGunResponse.class);
                processStatusRequest(response);

                String notificationId = null;
                if (null != response.getEventData() && null != response.getEventData().getUserVariables()) {
                    notificationId = response.getEventData().getUserVariables().get(ApplicationConstants.NOTIFICATION_ID);
                }

                if (null != notificationId && null != response) {
                    Notification notification = notificationRepository.findByNotificationId(notificationId);

                    if (null != notification) {
                        String eventStatus = response.getEventData().getEvent();
                        notification.setStatus(eventStatus);
                        if (eventStatus.equals(ApplicationConstants.DELIVERED)) {
                            notification.setNotificationDeliveredDate(new Date());
                        } else if (eventStatus.equals(ApplicationConstants.FAILED) || eventStatus.equals(ApplicationConstants.REJECTED)) {
                            notification.setNotificationFailureDate(new Date());
                        }
                        notificationRepository.save(notification);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Exception occured in processing request : " + e.getMessage());
            throw new RuntimeException(e);
        }

        log.error("filter method completed in WebhookEventFilter");
    }

    private void processStatusRequest(MailGunResponse response) {
        token = response.getSignature().getToken();
        timestamp = response.getSignature().getTimestamp();
        signature = response.getSignature().getSignature();

        String digest;
        if (timestamp != null && token != null) {
            digest = generatHmacDigest(timestamp + token, mailgunKey,
                    ApplicationConstants.HMAC_SHA256);
            /**
             * If signature is equal to digest, it means that request is valid
             */
            if (!signature.equals(digest)) {
                throw new RuntimeException("Authentication failed!");
            }
        }
    }

    private String generatHmacDigest(String msg, String keyString, String algo) {
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes(ApplicationConstants.UTF_8), algo);
            Mac mac = Mac.getInstance(algo);
            mac.init(key);

            byte[] bytes = mac.doFinal(msg.getBytes(ApplicationConstants.ASCII));

            StringBuilder hash = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            return hash.toString();
        } catch (UnsupportedEncodingException e) {
            log.error("Encoding is not supported : " + e.getMessage());
            throw new RuntimeException("Encoding format provided to generate signaure is not valid.", e);
        } catch (InvalidKeyException e) {
            log.error("Invalid key provided to generate signature : " + e.getMessage());
            throw new RuntimeException("Key provided to generate signaure is not valid.", e);
        } catch (NoSuchAlgorithmException e) {
            log.error("ALgorithm provided to generate signature is not valid : " + e.getMessage());
            throw new RuntimeException("Algorithm provided to generate signature is not valid.", e);
        }
    }


}


