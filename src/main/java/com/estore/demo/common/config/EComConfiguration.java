package com.estore.demo.common.config;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.jms.Session;

/**
 * Configuration class to create beans dynamically during startup of spring boot application
 */
@Configuration
public class EComConfiguration {

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.region.accessKey}")
    private String awsAccessKey;

    @Value("${cloud.aws.region.secretKey}")
    private String awsSecretKey;

    /*
    creates amazon sqs connection
     */
    public SQSConnectionFactory amazonSQSAsync() {
        return SQSConnectionFactory
                .builder()
                .withRegionName(region)
                .withAWSCredentialsProvider(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
                .build();
    }

    /*
    Tells spring to consider files to parse constants into user friendly messages
     */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:EComMessages");
        messageSource.setCacheSeconds(-1);
        return messageSource;
    }

    /*
    Factory container to set up jms connection
     */
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(amazonSQSAsync());
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setConcurrency("3-10");
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        factory.setMessageConverter(jacksonJmsMessageConverter());
        factory.setMaxMessagesPerTask(2);
        return factory;
    }

    /*
    JMS template to contain connection related parameters
     */
    @Bean
    public JmsTemplate defaultJmsTemplate() {
        JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(amazonSQSAsync());
        template.setMessageConverter(jacksonJmsMessageConverter());
        template.setPubSubDomain(false);
        return template;
    }

    /*
    Jackson convertor to convert POJO into a format that JMS can understand
     */
    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    /*
    Bean to initialize swagger
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }


}
