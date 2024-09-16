package com.example.jpaapplications;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.math.BigInteger;
import java.util.Date;

/**
 * JPA provider creates a persistence context which maintains a first level of data caching, happens at the java code level. This results in not interacting with the DB, unless it is required.
 * JPA does the DML only when its necessary by default. All we need is to be aware of this fact and write our code accordingly.
 *                                  DETACHED   (to only remove the entity from the managed state. It will not )
 *                                  ^     ||
 *                                  ||   merge(e)   [because in MANAGED state, there could be different version of "e", other than version of "E" in DETACHED state]
 *                          detach(e)    ||
 *                                 ||    \/
 * TRANSIENT(code) ==persist(e)==> MANAGED <==find() DATABASE
 *                                  ||  ^
 *                                  ||  ||
 *                          remove(e)  persist(e)
 *                                  ||  ||
 *                                  \/  ||
 *                                  REMOVED
 * entityManager.flush() moves all the entities with current changes in the MANAGED and REMOVED state to DB, to reflect those changes permanently.
 * entityManager.clear() clears every entity from the MANAGED state. Like .detach(e) but not for any specific entity and for every entity in the MANAGED state. I guess, it moves them in detached state.
 * entityManager.refresh() Entities in MANAGED state deviating from their state in DB will get updated back to its state as it is in DB, by running SELECT query. Don't know how entities coming from transient will behave.
 */
public class JpaPersistenceContextDemo {
    public static void main(String[] args) {
        Employee emp1 = new Employee(); // created an entity
        emp1.setName("New Employee");
        emp1.setSsn(new BigInteger("12345"));
        emp1.setDate(new Date());
        emp1.setEmployeeType(EmployeeType.FULL_TIME);   // up until now its normal java code, w/o having JPA context. Entity is in transient state, as we've created it but haven't done anything to it.

        System.out.println("********************** Created Employee Instance");

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myApp");
        EntityManager entityManager = entityManagerFactory.createEntityManager();   // Since till here, context must have been created, hence before moving ahead, JPA will run DDL.
        EntityTransaction transaction = entityManager.getTransaction();

        System.out.println("************************* Starting transaction");
        transaction.begin();

        entityManager.persist(emp1);    // insert didn't happen, but java code acts as it is in DB due to JPA caching. Entity is now in JPA managed state from transient state.
        // Entity is JPA managed doesn't necessarily mean that it has gone in DB. Similarly, entity coming from DB, is put in JPA managed state, where it was in DB.
        System.out.println("************************ After persist method is called.");

        Employee employeeFound = entityManager.find(Employee.class, 1); // Still, insert didn't happen. It's because its Hibernate only which created primary key in this case
        // Hence, no DB interaction for DML yet.
        System.out.println(employeeFound == emp1);  // TRUE, as both objects are same, and are managed by JPA caching.

        transaction.commit();   // this means, now JPA is bound to interact with the DB, hence now it performs the DML in the DB itself.
        System.out.println("************************** After transaction closed.");

        // There is a way to force implement all the changes that are currently in MANAGED state, in the database. Its called "flush()" which moves all the changes from MANAGED to DB.
        // This implements the values in the REMOVED state as well, into DB.
        entityManager.close();
        entityManagerFactory.close();
    }
}
