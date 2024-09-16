package com.example.jpaapplications;

import javax.persistence.*;
import java.util.Date;

@Entity
public class AccessCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Date issuedDate;
    private boolean isActive;
    private String firmwareVersion;

    @OneToOne(fetch = FetchType.EAGER)  // Once it'll go inside employee, we are mentioning JPA prior that there will be repeated mapping with this name.
    private Employee employee;  // We cannot put mappedBy = "accessCard" if we have put  mappedBy = "employee" in Employee in bi-di mapping. Only one could have.

//    The place where mapping annotation is defined, that entity owns the relation. Here, in this case of employee and access-card, both owns this bi-di relation.

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    @Override
    public String toString() {
        return "AccessCard{" +
                "id=" + id +
                ", issuedDate=" + issuedDate +
                ", isActive=" + isActive +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                '}';
    }
}
