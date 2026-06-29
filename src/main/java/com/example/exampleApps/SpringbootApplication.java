package com.example.exampleApps;

import com.example.exampleApps.freeCodeCamp.run.Location;
import com.example.exampleApps.freeCodeCamp.run.Run;
import com.example.exampleApps.freeCodeCamp.run.RunRepositoryJdbc;
import com.example.exampleApps.freeCodeCamp.user.UserHttpClient;
import com.example.exampleApps.freeCodeCamp.user.UserRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

// Below 2 lines can be written in preferred way as @SpringBootApplication(scanBasePackages = "com.example.exampleApps"). For multiple values, RHS must be as {"","",...,""}
@SpringBootApplication
@ComponentScan(basePackages = "com.example.exampleApps")	// To override the basePackages starting point; basePackages key is optional. This whole line is default and hence redundant because scanning by default starts from level where Application file is present. Multiple packages  must be in {"","","",...} format
//@EnableTransactionManagement	// this optional annotation used with transaction management and must be at the top of this class to enable declarative transaction in this app, so that wherever @Transactional is used, it'll work as expected. Otherwise, in absence of it, there is a high chance that @Transactional annotation will not work.
public class SpringbootApplication {	// It can be of any name

	// logger - LoggerFactory gives Logger object for a given class. We are making it private static final because we don't want it to get accessed and change its reference.
	private static final Logger LOG = LoggerFactory.getLogger(SpringBootApplication.class);

//	@Autowired	// This annotation cannot be local to any method, except setters. Its scope should be defined at class level.
	// Avoid using Field injection, as this bean might not be available in the IoC. In that case, null will be injected, making app prone to NPE.
	static WelcomeMessage welcomeMessage1;

	// Avoid using setter injection also.

	// Always use constructor injector, which makes sure that dependencies are injected only when its objects are present in IoC. Also, final attributes will not be overridden.
	@Autowired	// In case of single constructor class it's redundant to write @Autowired, container will try to inject the class dependency (constructor params).
	// In case of constructor injection in multi-constructor class, we need to explicitly mention @Autowired over that constructor which will be used to inject dependencies, mentioned as object param.
	public SpringbootApplication(WelcomeMessage welcomeMessage) {
		SpringbootApplication.welcomeMessage1 = welcomeMessage;
	}

	public static void main(String[] args) {

		// Original way to run the app; Remember its always SpringApplication.run, and NOT SpringbootApplication.run.   SpringApplication is in package org.springframework.boot already, ie, part of SBoot app.
//		SpringApplication.run(SpringbootApplication.class, args);

		ConfigurableApplicationContext context = SpringApplication.run(SpringbootApplication.class, args);		// context is only sent when app is successfully booted up, which means at this point IoC has minimum number of required beans in Spring Container|IoC Container to manage them for application.
//		context.close();	// destroys all beans, shuts down embedded web server, closes application DO NOT DO THIS for Web APIs. It can be helpful for short-lived scripts or cmd-line jobs (skip now, learn later)

		WelcomeMessage welcomeMessage3 = (WelcomeMessage) context.getBean("welcomeMessage");	// Discouraged. "Service Locator", aka, "BeanFactory Lookup". Internally, bean name is in camelCase. Use bean injection methods instead if this getBean is used for injecting dependency in a class.
		System.out.println(welcomeMessage3.getWelcomeMessage());

		var welcomeMessage2 = new WelcomeMessage();
		System.out.println(welcomeMessage2.getWelcomeMessage());

		System.out.println(welcomeMessage1.getWelcomeMessage());

		LOG.info("Your application has successfully started!!!");

		System.out.println(welcomeMessage1);	// same as 3
		System.out.println(welcomeMessage2);	// different from 1 and 3
		System.out.println(welcomeMessage3);	// same as 1
	}


	/**
	 * GO BACK NOW TO README IF CAME FOR THE FIRST TIME, AS IT IS ENOUGH. REST ALL WILL BE COVERED LATER.
	 */

	@Bean	// Used where we provide (external) configuration details to select the way to create an object of class required.
	UserHttpClient userHttpClient() {
		RestClient restClient = RestClient.create("https://jsonplaceholder.typicode.com/");
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient)).build();
		return factory.createClient(UserHttpClient.class);
	}



	// CommandLineRunner is something that run after the bean gets created, more specifically, after the application context has been created, which has all the bean in container.
	// CommandLineRunner is a Functional Interface, ie, interface is just having one abstract method. Hence, it can be used as a lambda expression, lambda can target this.
	// It thus saves time by avoiding implementing this class and overriding that one abstract method, which is only member of that interface.

	@Bean	// way of creating bean in an application context
//	CommandLineRunner runner() {	// We can use this if we are not calling runRepositoryJdbc for insertion after application starts.
	CommandLineRunner runner (RunRepositoryJdbc runRepositoryJdbc, UserRestClient restClient, UserHttpClient httpClient) {	// we are establishing a dependency injection of runRepositoryJdbc when Bean of CommandLineRunner gets created in application context.
		return args -> {
			Run run = new Run(1, "First Run", LocalDateTime.now(), LocalDateTime.now().plus(1, ChronoUnit.HOURS), 5, Location.OUTDOOR, null);
			LOG.info("From CommandLineRunner - Run: " + run);

			// Also, If we want to insert data into the DB as soon as application gets started, we can insert this Run object bean inside the DB, apart from inserting it from data.sql
			runRepositoryJdbc.create(run);

	// via Rest Client call
			System.out.println(restClient.findAll());
			System.out.println(restClient.findById(1));
// via Http client call
			System.out.println(httpClient.findAll());
			System.out.println(httpClient.findById(1));
		};
	}
}
