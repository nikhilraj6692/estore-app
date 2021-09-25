package com.estore.demo.common.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/*
Class to hold spring application context and perform bean operations
 */
@Component
@Scope(scopeName = "singleton")
public class ApplicationContextFactory implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /*
    sets application context
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationContextFactory.applicationContext = applicationContext;
    }

    /*
    get bean from bean name
     */
    public static <T> T getObject(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    /*
    get bean from bean name and argumnets that is to be set in constructor
     */
    public static <T> T getObject(String beanName, Object... args) {
        return (T) applicationContext.getBean(beanName, args);
    }

    /*
    checks if spring managed application context has bean created or not
     */
    public static boolean containsBean(String beanName) {
        return applicationContext.containsBean(beanName);
    }
}
