package com.example.jpaapplications.configurations;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import jakarta.persistence.EntityManagerFactory;

import javax.sql.DataSource;

@Configuration
public class AppConfig1 {   // this is for one DB configurations out of several DBs. Create EMfactory manager for each persistence unit.

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setJdbcUrl("jdbc:h2:mem:userDB");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setGenerateDdl(true);
        adapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
        return adapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean emf1 = new LocalContainerEntityManagerFactoryBean();
        emf1.setDataSource(dataSource);
        emf1.setJpaVendorAdapter(jpaVendorAdapter);
        emf1.setPackagesToScan("com.company.jpaapplications");
        emf1.setPersistenceUnitName("uniqueFactoryName");   // gives unique name for our entity manager factory
        return emf1;
    }

    // Lets say if we want to configure transaction manager factory, other than default resource-local, then ONLY DO BELOW IN SUCH CASES:
    @Bean
    public JpaTransactionManager transactionManager (EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
