package com.estore.demo.notification.service;

import com.estore.demo.common.exceptions.BusinessException;
import com.estore.demo.constants.ApplicationConstants;
import com.estore.demo.notification.domain.ChannelType;
import com.estore.demo.notification.domain.Notification;
import com.estore.demo.notification.domain.NotificationEvent;
import com.estore.demo.notification.domain.Template;
import com.estore.demo.notification.repo.ITemplateRepository;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.io.StringWriter;

/*
Abstract class to send notifications
 */
public abstract class INotificationChannel {
    protected ChannelType channelType;
    private ITemplateRepository templateRepository;
    private VelocityEngine velocityEngine;

    public INotificationChannel(ITemplateRepository templateRepository, VelocityEngine velocityEngine) {
        this.templateRepository = templateRepository;
        this.velocityEngine = velocityEngine;
    }

    public abstract Notification processNotification(NotificationEvent notificationEvent);

    /*
    parses .vm templates with dynamic placeholder and prepare final template content
     */
    public String parseMessage(NotificationEvent notificationEvent) {
        /*
        get template from database
         */
        Template template = templateRepository.findByEventNameAndChannelType
                (notificationEvent.getEventName(), this.channelType);

        notificationEvent.setTemplateName(template.getFileName());
        StringWriter stringWriter = new StringWriter();

        try {
            StringBuilder templatePath = new StringBuilder().append(ApplicationConstants.ROOT_PATH).append("/")
                    .append(template.getFileName()).append(ApplicationConstants.VM_EXTENSION);

            VelocityEngineUtils.mergeTemplate(velocityEngine, templatePath.toString(), "UTF-8",
                    notificationEvent.getData(), stringWriter);
        } catch (Exception e) {
            throw new BusinessException(ApplicationConstants.PARSE_ERROR);

        }
        return stringWriter.toString();
    }
}
