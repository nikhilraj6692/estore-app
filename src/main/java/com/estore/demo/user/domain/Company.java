package com.estore.demo.user.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

/*
POJO class to contain seller registered company details
 */
@Document(collection = "CompanyDetails")
public class Company {
    private String companyId;

    private String companyName;

    private String TAN;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTAN() {
        return TAN;
    }

    public void setTAN(String TAN) {
        this.TAN = TAN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(companyId, company.companyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId);
    }
}
