package com.example.jpaapplications;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JpaStarterWrite {
    public static void main(String[] args) {
        AccessCard card1 = new AccessCard();
        card1.setIssuedDate(new Date());
        card1.setActive(true);
        card1.setFirmwareVersion("1.0.0");

        AccessCard card2 = new AccessCard();
        card2.setIssuedDate(new Date());
        card2.setActive(false);
        card2.setFirmwareVersion("1.2.0");

        Employee employee1 = new Employee();
//        employee.setId(1);
        employee1.setName("Foo Bar");
        employee1.setDate(new Date(566454545));
        employee1.setEmployeeType(EmployeeType.CONTRACTOR);
//        employee1.setAccessCardId(card1.getId());
        employee1.setAccessCard(card1);
        employee1.setSsn(new BigInteger("123"));
        card1.setEmployee(employee1);

        Employee employee2 = new Employee();
//        employee.setId(1);
        employee2.setName("Baz Bar");
        employee2.setDate(new Date(566454545));
        employee2.setEmployeeType(EmployeeType.CONTRACTOR);
        employee2.setSsn(new BigInteger("00000"));

        Employee employee3 = new Employee();
//        employee.setId(1);
        employee3.setName("Baz Bar");
        employee3.setDate(new Date());
        employee3.setEmployeeType(EmployeeType.FULL_TIME);
//        employee3.setAccessCardId(card2.getId());
        employee3.setAccessCard(card2);
        employee3.setSsn(new BigInteger("1234567890421"));
        card2.setEmployee(employee3);

        // Now that we have employee instance and a place where DB is present, and we have mapped it to corresponding column of the mapped table, we will now access JPA API to save these entity to table.
        // The way to do that is by getting Entity Manager; it is an object service that JPA provides which responsible for creating and managing Entities.
        // Entity manager factory is created|bootstrapped using persistence unit name by JPA.
        // Below 2 lines are, JPA implementers ie hibernate here, creates and starts a persistence context when it starts up.
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myApp");
        EntityManager entityManager = entityManagerFactory.createEntityManager();


        // many-2-1 or 1-2-many
        PayStub payStub1 = new PayStub();
        payStub1.setPayPeriodStart(new Date());
        payStub1.setPayPeriodEnd(new Date());
        payStub1.setSalary(1000);
        payStub1.setEmployee(employee1);

        PayStub payStub2 = new PayStub();
        payStub2.setPayPeriodStart(new Date());
        payStub2.setPayPeriodEnd(new Date());
        payStub2.setSalary(2000);
        payStub2.setEmployee(employee1);

        employee1.setPayStub(List.of(payStub1, payStub2));  // Even though we have ManyToOne, yet we've complimented it with OneToMany because we cannot guarantee that
        // former is persisted in DB. The bean making process is still in Java code but not in DB. So, to make it consistent at java code level, we used here OneToMany also.

        EmailGroup emailGroup1 = new EmailGroup();
        emailGroup1.setName("Company water cooler discussion");
        emailGroup1.addEmployee(employee1);
        emailGroup1.addEmployee(employee3);

        EmailGroup emailGroup2 = new EmailGroup();
        emailGroup2.setName("Engineering");
        emailGroup2.addEmployee(employee1);

        employee1.addEmailGroup(emailGroup1);
        employee1.addEmailGroup(emailGroup2);
        employee3.addEmailGroup(emailGroup1);


        // Here we follow the imperative approach of transaction, where we write ourselves the GET txn, START txn, END txn, HANDLE ROLLBACK.
        // SpringBoot provide declarative approach of txn where a method around the actual DB operation using annotation.
        // @Transactional(propagation = _____)
        // @Transactional(readOnly = true|false(default))


        // Insert
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(employee1);
        entityManager.persist(employee2);
        entityManager.persist(employee3);

        entityManager.persist(card1);
        entityManager.persist(card2);

        entityManager.persist(payStub1);
        entityManager.persist(payStub2);

        entityManager.persist(emailGroup1);
        entityManager.persist(emailGroup2);

        transaction.commit();

        // Select
        Employee fetchedEmployee = entityManager.find(Employee.class, 1);
        System.out.println(fetchedEmployee);


        // Update - First fetch|find the entity from DB. Then change the object attribute. Then send it back to DB. It smartly decides whether to insert or update using PK.
        transaction.begin();
        fetchedEmployee.setEmployeeType(EmployeeType.FULL_TIME);
        entityManager.persist(fetchedEmployee);
        transaction.commit();

        // Delete - First fetch and then delete
        Employee fetchedEmployee2 = entityManager.find(Employee.class, 2);
        transaction.begin();
        entityManager.remove(fetchedEmployee2);
        transaction.commit();


        entityManager.close();
        entityManagerFactory.close();
    }
}
