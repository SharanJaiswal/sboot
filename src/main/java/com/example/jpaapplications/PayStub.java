package com.example.jpaapplications;

import javax.persistence.*;
import java.util.Date;

/**
 * 1-2-many: 1 employee can have multiple paystubs. A paystub can only belong to 1 employee. multiple paystubs can belong to 1 employee. No 1 paystub can belong to multiple employee.
 * It'll be difficult for Employee entity to own an 1 to many relation, as 1 emp can have multiple paystub. If proceeded with it, it'll need collection in java but it wont estblisg DB relational mapping.
 * Hence, it'll be easier to make PayStub own 1-2-many relation. In this case, we are not ready to repeat employee tuple just to make it own 1-2-many mapping with paystub.
 */

@Entity
public class PayStub {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Date payPeriodStart;
    private Date payPeriodEnd;
    private float salary;
    @ManyToOne
    @JoinColumn(name = "payStub_for")
    private Employee employee;

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

    public Date getPayPeriodStart() {
        return payPeriodStart;
    }

    public void setPayPeriodStart(Date payPeriodStart) {
        this.payPeriodStart = payPeriodStart;
    }

    public Date getPayPeriodEnd() {
        return payPeriodEnd;
    }

    public void setPayPeriodEnd(Date payPeriodEnd) {
        this.payPeriodEnd = payPeriodEnd;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "PayStub{" +
                "id=" + id +
                ", payPeriodStart=" + payPeriodStart +
                ", payPeriodEnd=" + payPeriodEnd +
                ", salary=" + salary +
                ", employee=" + employee +
                '}';
    }
}
