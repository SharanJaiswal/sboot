package com.example.jpaapplications;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaStarterMain {
    public static void main(String[] args) {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("Foo Bar");

        // Now that we have employee instance and a place where DB is present, and we have mapped it to corresponding column of the mapped table, we will noe access JPA API to save these entity to table.
        // The way to do that is by getting Entity Manager; it is an object service that JPA provides which manages Entity.
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myApp");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.persist(employee);
    }
}
