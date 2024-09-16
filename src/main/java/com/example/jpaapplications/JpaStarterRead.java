package com.example.jpaapplications;

import javax.persistence.*;

public class JpaStarterRead {
    public static void main(String... args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myApp");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // by default in 1-2-1 JPA will perform he left outer join b/n emp and card table. Good in a way. Eagerly fetching.
        Employee employee1 = entityManager.find(Employee.class, 1);
//        System.out.println(employee1);  // this means fetch everything, along with the related entities because internally its calling card details also.
        System.out.println(employee1.getName());
        System.out.println("*******Fetched employee***********Fetching Card********");
        System.out.println(employee1.getAccessCard());
        System.out.println(employee1.getPayStub());


        // If we fetch access card, it will not fetch the employee details along with it. Although we can make query to run it on server, JPA is not by default pulling the employee detail who owns this card.
        // One way is to put 1-2-1 annotated employee attr in access card class. This will not create the deadlock because JPA beforehand makes the IDs for the entities before persisting those entities.
        // This creates the 2 way relationship. emp->card and card->emp. But this will do 2 joins making redundant joins.
        // To avoid, we mark one mapping as primary relationship, and one to be mirrored of primary. So we use "mappedBy" on primary relationship.
        System.out.println("**********Accessing the entity via access card*************");
        AccessCard card2 = entityManager.find(AccessCard.class, 2);
        System.out.println(card2);
        System.out.println(card2.getEmployee());
    }
}
