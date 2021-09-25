package com.estore.demo.common.aspectj;

import com.estore.demo.common.context.ApplicationContextFactory;
import com.estore.demo.common.context.LoggedInUser;
import com.estore.demo.common.exceptions.AccessDeniedException;
import com.estore.demo.user.domain.RoleBasedCapabilityAccess;
import com.estore.demo.user.repo.IRBACRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Aspect
@Configuration
public class RequiredPermissionAspect {

    private final Logger log = LoggerFactory.getLogger(RequiredPermissionAspect.class);

    /*
    Aspect advice to validate capability and permissions of the logged in user.
     */
    @Before(value = "@annotation(com.estore.demo.common.aspectj.RequiredPermission) && execution(* *(..))")
    public void invokeProcessor(JoinPoint joinPoint) {
        log.debug("advice exectutions starts");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        RequiredPermission annotation = method.getAnnotation(RequiredPermission.class);

        LoggedInUser loggedInUser = ApplicationContextFactory.getApplicationContext().getBean(LoggedInUser.class);
        String userId = loggedInUser.getUserId();
        if (null == userId) {
            throw new AccessDeniedException();
        }

        String capabilityID = annotation.capabilityId();
        RequiredPermission.PermissionValue permissionValue = annotation.permission();
        String permissionOnApi = permissionValue.name();

        Collection<? extends GrantedAuthority> roles = loggedInUser.getAssignedRoles();
        List<String> list = new ArrayList<>();
        for (GrantedAuthority role : roles) {
            list.add(role.toString());
        }

        //check for permission
        checkForPermission(list.get(0), capabilityID, permissionOnApi);
        log.debug("advice exectutions ends");
    }

    /*
    It checks whether permission given to the user against user role is valid enough to enter resource layer
     */
    private void checkForPermission(String role, String capabilityID, String permissionOnApi) {
        IRBACRepository rbacRepository = ApplicationContextFactory.getApplicationContext().getBean(IRBACRepository.class);
        List<RoleBasedCapabilityAccess> accessList = rbacRepository.findByRole(role);

        boolean accessMatch = false;

        for (RoleBasedCapabilityAccess access : accessList) {
            if (capabilityID.equalsIgnoreCase(access.getCapabilityId()) &&
                    access.getPermissions().contains(permissionOnApi)) {
                accessMatch = true;
                break;
            }
        }

        if (!accessMatch)
            throw new AccessDeniedException();

    }
}