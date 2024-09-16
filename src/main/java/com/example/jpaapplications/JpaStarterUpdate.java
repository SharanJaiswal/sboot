package com.example.jpaapplications;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaStarterUpdate {
    public static void main(String... args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myApp");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Employee employee1 = entityManager.find(Employee.class, 3);
        EmailGroup emailGroup1 = entityManager.find(EmailGroup.class, 6);

        employee1.addEmailGroup(emailGroup1);
        emailGroup1.addEmployee(employee1);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
    // We added/updated the data in the table.
        entityManager.persist(emailGroup1);
        entityManager.persist(employee1);

        transaction.commit();
    }
}
