package com.example.jpaapplications;

import javax.persistence.*;
import java.util.List;

/**
 * Always use reference of class and its attributes in JPQL to make SQL query with table name (class name) and column name (class attributes).
 */
public class JpaJPQLExample {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myApp");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

//        Query query = entityManager.createQuery("select e from Employee e");
//        List resultList = query.getResultList();    // Here we've generic List because we have not mentioned the type of the elements of the list anywhere.
        TypedQuery<Employee> query = entityManager.createQuery("select e from Employee e where e.id > 1 order by e.name desc", Employee.class);
        List<Employee> resultList = query.getResultList();
        resultList.forEach(System.out::println);

//        TypedQuery<Employee> query1 = entityManager.createQuery(
//                "select e.date, e.employeeType, e.name, e.ssn from Employee e join AccessCard a on e.id = a.employee_id",
//                Employee.class);
//        List<Employee> resultList1 = query1.getResultList();
//        resultList1.forEach(System.out::println);
// The above query is flawed as there is no "employee_id" attribute in AccessCard class. What we can do is use "a.employee.id" to perform join using already joined column.


        System.out.println("************************* Querying more than 1 attributes where tuple doesn't fit in any pre-defined Entity class *************************");
        // Now suppose we want tuple in query result set of NOT to be of type as any already defined entity class. Then, if there is single attribute is only queried, then we can use eg TypedQuery<String> etc.
        // But if we are querying >1 attributes which doesn't collectively fit in the list of type any pre-defined entity, then we need different approach.
        Query query2 = entityManager.createQuery("select e.name, e.ssn from Employee e");
        // Since, result will be the LIST OF NON-GENERIC TYPE, where LIST WILL HAVE EACH ELEMENT AS AN ARRAY OF OBJECT, OR OBJECT ARRAY.
        List resultList2 = query2.getResultList();
        System.out.println(resultList2);   // [[Ljava.lang.Object;@21263314, [Ljava.lang.Object;@6ca30b8a]  // We can see that array have element of type Object.

        TypedQuery<Object[]> query3 = entityManager.createQuery("select e.name, e.ssn from Employee e", Object[].class);
        List<Object[]> listOfObjectArray = query3.getResultList();
        listOfObjectArray.forEach(e -> System.out.println(e[0] + " === " + e[1]));


        // demonstration of named query:
        TypedQuery<Employee> empNameAsc = entityManager.createNamedQuery("emp name asc", Employee.class);
        empNameAsc.setParameter("eyedee", 4);
        List<Employee> resultList3 = empNameAsc.getResultList();
        resultList3.forEach(System.out::println);


        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        transaction.commit();

        entityManager.close();
        entityManagerFactory.close();
    }
}
