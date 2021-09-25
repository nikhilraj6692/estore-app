package com.estore.demo.common.aspectj;

import com.estore.demo.constants.ApplicationConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RBAC annotation used to validate the capability and permissions of logged in user. All rest resources which are to be
 * authorized should be annotated with this annotation
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredPermission {

    String capabilityId() default "NOT_CONFIGURED";

    PermissionValue permission();

    public enum PermissionValue {

        READ {
            @Override
            public String toString() {
                return ApplicationConstants.READ;
            }
        },

        CREATE {
            @Override
            public String toString() {
                return ApplicationConstants.WRITE;
            }
        },

        UPDATE {
            @Override
            public String toString() {
                return ApplicationConstants.UPDATE;
            }
        },

        DELETE {
            @Override
            public String toString() {
                return ApplicationConstants.DELETE;
            }
        }
    }


}