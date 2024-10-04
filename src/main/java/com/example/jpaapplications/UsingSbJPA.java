package com.example.jpaapplications;

import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.transaction.Transactional;

@Component
@Transactional  // to implement transactions. Adding this annotation over here on class ensures to apply it on every public method inside this class.
// Alternatively, we can specifically add this annotation on selective public methods of the class, that controls the critical section.
public class UsingSbJPA {
    // Transaction management in springboot uses AOP which invokes an interceptor.

    /**
     * Hierarchy of transaction managers:
     *                  <<interface>> "TransactionManager", an empty interface.
     *                  <<interface>> "PlatformTransactionManager" extending the first, having getTransaction(), commit() and rollback() methods to implement.
     *                  <<abstract>> "AbstractPlatformTransactionManager", extending the second, and having the default implementation of above 3 methods.
     *
     * This 3rd class is getting extended by concrete txn mgr classes :
     *          "DataSource TxnMgr, Hibernate TxnMgr, JPA TxnMgr, JTA TxnMgr. {First 3: manages local txns. 4th manages distributed txn (2-phase commit)}
     *          DataSource txnmgr is also extended by JDBC TxnMgr.
     *
     * 2 approach to manage txn: Declarative; Programmatic 
     * DECLARATIVE: when we use annotations to manage txn,many things are abstracted behind the scene by springboot. eg, @Transactional will choose respective NoSQL or SQL data source based on its decision(mostly JPA). See AppConfig.
     *
     * In case where we need to choose specific txn manager, ie, JPA or JDBC or Hibernate, etc. (to write our own query in JDBC), then we've to use @Configuration on AppConfig class to configure our app.
     *
     * PROGRAMMATIC: Where we maintain the place to manage the txn. For that, we have 2 ways:
     * 1. first need a txn manager component. And then use it for managing the txns. We implemented it in another class.
     * 2. We can use TXN templates. We implemented it in another class.
     */
}

@Component
class ProgrammaticPart1 {
    PlatformTransactionManager userTransactionManager;

    public ProgrammaticPart1(PlatformTransactionManager userTransactionManager) {  // we can use any approach of txn manager object. Here, we have created this in the AppConfig
        this.userTransactionManager = userTransactionManager;
    }

    public void updateUserProgram() {
        TransactionStatus status = userTransactionManager.getTransaction(null); // start a txn
        try {
            System.out.println("Insert some query");
            System.out.println("Update some query");
            userTransactionManager.commit(status);
        } catch (Exception e) {
            userTransactionManager.rollback(status);
        }
    }
}


@Component
class ProgrammaticPart2 {
    TransactionTemplate transactionTemplate;

    public ProgrammaticPart2(TransactionTemplate transactionTemplate) { // we can use any approach of txn manager object. Here, we have created this in the AppConfig
        this.transactionTemplate = transactionTemplate;
    }

    public void updateUserProgram() {
        TransactionCallback<TransactionStatus> dbOperationsTask = (TransactionStatus status) -> {   // callback is the business logic that needs to be executed.
            System.out.println("insert query");
            System.out.println("update query");
            return status;
        };
        TransactionStatus status =  transactionTemplate.execute(dbOperationsTask);  // see inside the execute function.
    }
}


