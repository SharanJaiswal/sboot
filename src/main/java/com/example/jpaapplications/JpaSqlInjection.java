package com.example.jpaapplications;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class JpaSqlInjection {
    public static void main(String... args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myApp");
        EntityManager entityManager = entityManagerFactory.createEntityManager();


        int id = 4; // type of param can be of any type if its value can be converted to String type, provided query.setParameter() is not used.
        // If query.setParameter() is used, then type of that param should be of type compatible of type of attribute defined in the entity class.
//        id += "; drop table employee_data";
        TypedQuery<Employee> query1 = entityManager.createQuery(
                "select e from Employee e where id < " + id,
                Employee.class
        );
        List<Employee> resultList1 = query1.getResultList();
        resultList1.forEach(System.out::println);

        // But "ID" coming from upstream request might contain some other query. To avoid, use below strategy.
        TypedQuery<Employee> query2 = entityManager.createQuery(
                "select e from Employee e where id < :id",
                Employee.class
        );
        query2.setParameter("id", id);  // This detects presence of SQL query. If found, throws an error.
        List<Employee> resultList2 = query2.getResultList();
        resultList2.forEach(System.out::println);

        entityManager.close();
        entityManagerFactory.close();
    }
}
