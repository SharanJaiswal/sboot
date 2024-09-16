package com.example.exampleApps;

import com.example.exampleApps.freeCodeCamp.run.Location;
import com.example.exampleApps.freeCodeCamp.run.Run;
import com.example.exampleApps.freeCodeCamp.run.RunRepositoryJdbc;
import com.example.exampleApps.freeCodeCamp.user.UserHttpClient;
import com.example.exampleApps.freeCodeCamp.user.UserRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@SpringBootApplication
@ComponentScan(basePackages = "com.example.exampleApps")	// basePackages key is optional. This whole line default and hence redundant because scanning by default starts from level where Application file is present.
//@EnableTransactionManagement	// this annotation used with JPA and must be at the top of this class to enable declarative transaction in this app, so that wherever @Transactional is used, it'll work as expected.
public class SpringbootApplication {

	// logger - LoggerFactory gives Logger object for a given class. We are making it private static final because we don't want it to get accessed and change its reference.
	private static final Logger LOG = LoggerFactory.getLogger(SpringBootApplication.class);

//	@Autowired
	// This annotation cannot be local to any method. Its scope should be defined at class level.
	static WelcomeMessage welcomeMessage1;

	public SpringbootApplication(WelcomeMessage welcomeMessage) {
		SpringbootApplication.welcomeMessage1 = welcomeMessage;
	}

	public static void main(String[] args) {

		// Original way to run the app
//		SpringApplication.run(SpringbootApplication.class, args);

		ConfigurableApplicationContext context = SpringApplication.run(SpringbootApplication.class, args);
//		context.close();	// closes application DO NOT DO THIS.
		WelcomeMessage welcomeMessage3 = (WelcomeMessage) context.getBean("welcomeMessage");	// Internally, bean name is in camelCase
		System.out.println(welcomeMessage3.getWelcomeMessage());

		var welcomeMessage2 = new WelcomeMessage();
		System.out.println(welcomeMessage2.getWelcomeMessage());

		System.out.println(welcomeMessage1.getWelcomeMessage());

		LOG.info("Your application has successfully started!!!");

	}


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
			LOG.info("Run: " + run);

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
