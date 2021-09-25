package com.estore.demo.user.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/*
POJO class to hold capabilities and permissions of a user depending upon role. This will help to validate against aspect advice
 */
@Document(collection = "RoleBasedCapabilityAccess")
public class RoleBasedCapabilityAccess {
    private String capabilityId;

    private String role;

    private List<String> permissions;

    public String getCapabilityId() {
        return capabilityId;
    }

    public void setCapabilityId(String capabilityId) {
        this.capabilityId = capabilityId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }


}
