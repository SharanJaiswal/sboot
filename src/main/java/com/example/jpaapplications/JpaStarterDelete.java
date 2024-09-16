package com.example.jpaapplications;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * TRANSIENT ==persist(e)==> MANAGED <==find() DATABASE
 *                           ||  ^
 *                           ||  ||
 *                   remove(e)  persist(e)
 *                           ||  ||
 *                           \/  ||
 *                          REMOVED
 *
 * When entity is removed, it is no longer managed and it is moved to REMOVED state, which is just like any other JPA state. It doesn't mean that it is deleted from DB.
 * But, just nt been managed by JPA as of now. To make it managed back by JPA, we can call persist(entity) method again on the removed entity.
 */
public class JpaStarterDelete {
    public static void main(String... args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myApp");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        Employee employee1 = entityManager.find(Employee.class, 3);
        Employee employee2 = entityManager.find(Employee.class, 1);


        transaction.begin();
// By default, deleting a row which is getting referenced by other table's row which has data, other than intermediate tables of m2m, will not get deleted.
        // But deleting a row which is only getting referenced by some intermediate table, and not any other table having data referring this row, is possible.
        // To make it work, where due to referential integrity deletion is blocked, we can mention cascade type
        entityManager.remove(employee1);
        entityManager.remove(employee2);

        transaction.commit();

    }
}
/**
 * SELECT * FROM TRANSITIVEEMPLOYEEEMAILTABLE ;
 * SELECT * FROM EMAILGROUP ;
 * SELECT * FROM PAYSTUB ;
 * SELECT * FROM EMPLOYEE_DATA ;
 * SELECT * FROM ACCESSCARD ;
 */