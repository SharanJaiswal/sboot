package com.example.exampleApps.cac.appconfigurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Configuration  // Way to override "convention over configuration", where we provide configuration to spring boot to override specific autoconfiguration.
// Higher precedence over autoconfiguration. This contains @Component inside it.
public class AppConfig {
//    @Bean   // Here, as external configuration, we are telling Springboot to create an object of some User class, because we cannot use @Component over User class as we don't its default constructor.
//    @Bean(name="someRandomName")  // This will give this specific name to the created bean in the IoC.
//    public User createUserBean() {    // we can give here any method name. But return type decides here which class' object needs to be created. Object bean name will be same as method name, here 'createUserBean' not 'user' as bean name.
//        return new User("defaultUserName", "defaultEmail");
//    }
//    If for certain class Bean as well as Component is present, then Bean will be given precedence as it is inside configuration class. I'm unsure about if both will get created or not. Use @Primary to give precedence maybe.

//    If more than 1 bean of same type is present, then IoC will create and manage both bean, give them different names. We can also give them name using qualifier.
//    @Bean   // It is method level annotation unlike @Component which is class level annotation.
//    public User createUserBean() {
//        return new User("specificUserName", "specificEmail");
//    }


    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.driver");
        dataSource.setUrl("jdbc:h2:mem:testdb");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager userTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
        /**
        return new HibernateTransactionManager(dataSource);
        return new JdbcTransactionManager(dataSource);
        return new JpaTransactionManager(); // default picked by springboot
         */
    }
    // Now, we will use ---   @Transactional(transactionManager="userTransactionManager")  --- over class/methods where we intend to use specific transaction manager. We provided bean name.


    // This is specific to programmatic approach, to use transaction templates.
    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager userTransactionManager) {
        return new TransactionTemplate(userTransactionManager);
    }
}
