package com.example.jpaapplications;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "EMPLOYEE_DATA")  // ,schema = "", catalog = ""
@NamedQuery(query = "select e from Employee e where e.id < :eyedee order by e.name", name = "emp name asc") // this can be used more than 1 times over entity with >=JAVA11
public class Employee {

    @Id // could be any primitive non-floating data type like char,byte,int,long,short and their wrapper types + String. Avoid using 'long' due to its precision issue.
//    @Column(name = "") and many other important key value pairs are also present
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SEQUENCE:sequence of value managed by DB. TABLE: way in which DB creates one table and manages it. AUTO:default - selects DB preferred way automatically
    private int id; // primitive dta types has respective default value in java if we don't set it from here to insert, but DB can allow these attributes to be null.
    private String name;

    @Column(unique = true, nullable = false, updatable = false)    // and many other constraints
    private BigInteger ssn;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Enumerated(EnumType.STRING)    // ORDINAL as default, we are now populating in DB as varchar|string. But this doesn't promise consistency.
    private EmployeeType employeeType;  // by default, JPA maps the ordinals of the enum members as integers in the DB, not as enum. eg: 0,1,2,..., if we don't annotate

    @Transient  // way to not get it saved in DB automatically. Recommended only for JPA purposes.
    private String debugString; // we can also use "transient" keyword after "private", which is not recommended when we are strictly using for JPA purpose.

//    private int accessCardId;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.REMOVE)   // put annotation above FK column, where FK column automatically  will the PK of related entity.
    // see the accesscard table also where 1-2-1 mapping is present. If in both 1-2-1 bi-di mapping we don't provide "mappedBy", then both table will have attribute FK referencing PK of the other table.
    // But, in 1-2-1 bidi mapping, we can only put "mappedBy" at 1 place, either of them. Here it signifies that in AccessCard entity, this 1-2-1 mapping will be controlled by accesscard table, with employeeID column.
    // Now, since "mappedBy" puts column as FK in the mentioned table,therefore this employee table will not have FK column referencing the ID of Access cards.
    // Now since employee table won't have FK column, therefore, we can try to establish many-2-1 in accessCard table, simply by just trying to add the data for same employee.
    // The above attempt will then make 1-2-many mapping in employee table, as 1 employee have >1 access card.
    // Worst part is, that w/o "mappedBy" also we can tamper the integrity of the 1-2-1 mapping of either of the table.
    // Integrity will be checked when 2 related tables will get mapped eg during joining. At that time, type of mapping will be checked.
    private AccessCard accessCard;  // This is not a column, but the entity itself which is related to this entity. JPA will auto-pick the related column.

    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE)   // we are registering here that PayStub table has this attribute's complimentary mapping with name employee, to reduce overhead.
    private List<PayStub> payStub = new ArrayList<>();  // we are gonna convenience method to make data consistent 2-way.

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "transitiveEmployeeEmailTable",
    joinColumns = @JoinColumn(name = "members_id"), // refer to that column in the transient table whose column is referring to the joined column of the entity that owns relationship.
    inverseJoinColumns = @JoinColumn(name = "subscribed_email_group"))  // refer to that column in the transient table whose column is referring to the joined column of this attribute.
    private List<EmailGroup> emailGroups = new ArrayList<>();

    public void addPayStub(PayStub payStub) {   // we must call this method as soon as we put employee in a paystub object. This will add paystub in employee, for maintaining consistency.
        this.payStub.add(payStub);
    }

    public void addEmailGroup(EmailGroup emailGroup) {
        this.emailGroups.add(emailGroup);
    }

    public List<EmailGroup> getEmailGroups() {
        return emailGroups;
    }

    public void setEmailGroups(List<EmailGroup> emailGroups) {
        this.emailGroups = emailGroups;
    }

    public List<PayStub> getPayStub() { // this won't get reflected in the table visually, if queried. But this column is there in the server.
        return payStub;
    }

    public void setPayStub(List<PayStub> payStub) {
        this.payStub = payStub;
    }

    public AccessCard getAccessCard() {
        return accessCard;
    }

    public void setAccessCard(AccessCard accessCard) {
        this.accessCard = accessCard;
    }

    public String getDebugString() {
        return debugString;
    }

    public void setDebugString(String debugString) {
        this.debugString = debugString;
    }

//    public int getAccessCardId() {
//        return accessCardId;
//    }
//
//    public void setAccessCardId(int accessCardId) {
//        this.accessCardId = accessCardId;
//    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getSsn() {
        return ssn;
    }

    public void setSsn(BigInteger ssn) {
        this.ssn = ssn;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
