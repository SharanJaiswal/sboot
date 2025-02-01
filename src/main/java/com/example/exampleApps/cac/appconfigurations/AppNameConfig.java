package com.example.exampleApps.cac.appconfigurations;

import com.example.exampleApps.cac.filters.CustomFilter1;
import com.example.exampleApps.cac.filters.CustomFilter2;
import com.example.exampleApps.cac.inerceptors.ControllerCustomInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.orm.hibernate5.HibernateTransactionManager;
//import org.springframework.orm.jpa.JpaTransactionManager;


@Configuration  // Way to override "convention over configuration", where we provide configuration to spring boot to override specific autoconfiguration.
// Higher precedence over autoconfiguration. This contains @Component inside it.
//@Import(RandomConfigAppName.class)
public class AppNameConfig implements WebMvcConfigurer {    // implemented this only for registering the custom interceptors.
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

    // FOR INTERCEPTOR

    @Autowired  // because we have annotated Component. Otherwise, we would have used "new" to inject dependency.
    ControllerCustomInterceptor customeInterceptor1;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(customeInterceptor1)
                .addPathPatterns("/api/*")  // apply to these URL patterns
                .excludePathPatterns("/api/updateUser", "/api/deleteUser"); // Exclude these URL patterns

        // Registering another interceptor. Order of registering will decide the order of interceptor execution, ie, all pre methods will follow interceptor registration order with requests, while all post and subsequently all after methods will follow reverse order with response.
//        registry.addInterceptor(customeInterceptor2)
//                .addPathPatterns("/api/*")  // apply to these URL patterns
//                .excludePathPatterns("/api/updateUser", "/api/deleteUser"); // Exclude these URL patterns
    }


    // Filters::::
    @Autowired private CustomFilter1 customFilter1;
//    @Autowired private CustomFilter2 customFilter2;   //

    @Bean
    public FilterRegistrationBean<CustomFilter1> registerFilter1 () {
        FilterRegistrationBean<CustomFilter1> customFilter1FilterRegistrationBean = new FilterRegistrationBean<>();
        customFilter1FilterRegistrationBean.setFilter(customFilter1);
        customFilter1FilterRegistrationBean.setOrder(2);
        customFilter1FilterRegistrationBean.addUrlPatterns("/api/*");
        return customFilter1FilterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<CustomFilter2> registerFilter2 (CustomFilter2 customFilter2) {
        FilterRegistrationBean<CustomFilter2> customFilter1FilterRegistrationBean = new FilterRegistrationBean<>();
        customFilter1FilterRegistrationBean.setFilter(customFilter2);
//        customFilter1FilterRegistrationBean.setOrder(2);  // we can write here the custom logic to set precedent also.
        customFilter1FilterRegistrationBean.addUrlPatterns("/api/*");
        return customFilter1FilterRegistrationBean;
    }


//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.h2.driver");
//        dataSource.setUrl("jdbc:h2:mem:testdb");
//        dataSource.setUsername("sa");
//        dataSource.setPassword("");
//        return dataSource;
//    }
//
//    @Bean
//    public PlatformTransactionManager userTransactionManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//        /**
//        return new HibernateTransactionManager(dataSource);
//        return new JdbcTransactionManager(dataSource);
//        return new JpaTransactionManager(); // default picked by springboot
//         */
//    }
//    // Now, we will use ---   @Transactional(transactionManager="userTransactionManager")  --- over class/methods where we intend to use specific transaction manager. We provided bean name.
//
//
//    // This is specific to programmatic approach, to use transaction templates.
//    @Bean
//    public TransactionTemplate transactionTemplate(PlatformTransactionManager userTransactionManager) {
//        return new TransactionTemplate(userTransactionManager);
//    }
}
