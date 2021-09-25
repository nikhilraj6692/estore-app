package com.estore.demo.user.domain;

import java.util.Set;

/*
POJO class to contain seller related info
 */
public class Seller extends User{
    private String companyId;

    private Set<String> postedProductIds;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Set<String> getPostedProductIds() {
        return postedProductIds;
    }

    public void setPostedProductIds(Set<String> postedProductIds) {
        this.postedProductIds = postedProductIds;
    }
}



