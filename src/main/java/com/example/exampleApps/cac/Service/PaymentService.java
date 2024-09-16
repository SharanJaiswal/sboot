package com.example.exampleApps.cac.Service;

import com.example.exampleApps.cac.DTO.PaymentRequest;
import com.example.exampleApps.cac.DTO.PaymentResponse;
import com.example.exampleApps.cac.Entity.PaymentEntity;
import com.example.exampleApps.cac.Repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    /*
    Dependency injection need: If 2 classes are tightly coupled, then changes in dependency class will impact the dependent class. eg., If dependency class becomes interface, then in dependent class original object creation code will fail.
    So, it is breaking the dependency inversion principle of SOLID where dependent class depends on concrete implementation. It should depend on abstraction.
    Dependency can be injected using constructor of dependent class, by passing the object of concrete implementation class of dependency into interface type ref-var in dependent class; or by any method.
    But this step can also be managed by spring, making it loosely coupled. So, dependency object is looked in IoC container. If ot found, then created, managed by spring, injected.
    Spring uses reflection to look iteratively one-by-one all the fields with @Autowired and inject its dependency. Spring @Autowired do not work on immutable(final) fields, if its value has not been initialized.
    But, if its initialized with "null" or any object, then since reflection came into picture and it doesn't care about immutability, it'll replace the existing referencing value with auto-injected bean.

    In cases where a class has @Autowired dependency, and that former class is initialized via "new" keyword, then in such cases former class won't auto-inject dependency because it happens by spring, not by user defined code, throwing NPE.
    Creating bean of autowired attributes becomes difficult during unit testing. So, using reflection, via @Mock private DependencyClass dependecyMockObj; @InjectMock DependentClass dependentClassObject; helps to achieve this.

    @Autowired(required = true|false)   // w/o quotes, w/o key as 'value', default is true. It tells spring app that if required is true means spring should have it injected in any case, false means if object is not created or injected then proceed w/o injection.
    false is generally used when using @ConditionalOnProperty(prefix="prop.prefix", value="prop.suffix", havingValue="anyMatchingValueLiketruefalsesharan", matchIfMissing=propValueWithoutQuotes) over class whose bean to be created.
    It is used in cases, eg, When NoSQLBean is required over MysqlBean; when 2 apps using same code bases and that same code base need to make decision to initialize specific bean based on type of app accessing that codebase.
    Second example can also be achieved using @Profile tag but use @Conditio.... tag if possible in cases where single decision doesn't need to be made based on environment of application.

    We can also use @Autowired over a setter method with any name with void return type, accepting object to be injected, and setting the param object to dependency reference variable of the dependent class; setter fully managed by spring.
    This case using setters is useful when we want to auto-inject a default object to dependent fields of a dependent class, also when we want change the dependency at runtime using defined code as we can also call these setters manually.
    This also helps to get rid of @InjectMocks and @Mocks. But, only disadvantage is that we cannot make the dependent attributes immutable, if we are using simple non-synchronised setter w/o reflection.
    Object injection using setter can create readability and maintainability issues as per standards where objects should be created at start, if possible.

    Instead of simply putting @Autowired over a dependent attribute of dependent class, we can put this tag over dependent class constructor with params including all|few dependent attributes and set the values of these object params to corresponding required dependent attributes.
    This will change the order of creation dependency and dependent object; where first Dependent class obj was created -> its constructor called -> reflection is used to find @Autowired dependent fields -> dependency objects created -> injected ....
    to ... Dependency attributes objects will be created first -> injected using constructor of dependent object -> dependent object is created.

    Using @Autowired over class constructor is not mandatory if constructor count is 1 in a dependent class POST SPRING 4.3. But if >1 constructors are present then app fails to start because spring failed to pick constructor to inject dependencies.
    Helpful in cases where we want to initialize only specific|mandatory attributes at app bootup (maybe to avoid NPE and null checks), and rest on demand whose classes are annotated with @Lazy.
    Also, in cases where attributes are initialized using autowired constructors, but attributes are declared as immutable. They are initialized using autowired constructors.
    Makes unit testing easy, as we can first create the dependency objects mocks, and then call the constructor of class under test by passing the mock object to it.

    ISSUES WITH DEPENDENCY INJECTION: Circular dependency; Unsatisfied dependency
    Soln for Circular dependency where circular dependency attributes in both classes are @Autowired:
    1. Take out the common code from both classes and put it in new separate class.
    2. Place @Lazy over the @Autowired attribute in one of the class, which will put a proxy dependency object and skip creating actual dependency object unless required in code.
    3. @Lazy can also be used where circular dependency is implemented using autowired setters. So, place a @Lazy over autowired setter of one class to break the circular dependency and put the proxy object.

    Solution for Unsatisfied dependency (happens when while trying to inject an object to a dependency attribute of type interface having multiple implementations. This will create confusion for spring to chose which implementation of interface):
    1. Annotate one of the concrete implementation class of interface with @Primary tag to resolve the decision over confusion.
    2. put @Qualifier("objectName_X") over each concrete class implementation, and then put @Qualifier("objectName_X") over autowired dependent dependency with the qualifier name to which precedence should be given.
    But second way is violating the dependency inversion principle, as we are hardcoding the type of object to be injected. This could be resolved in 2 ways:
        A. declare all autowired reference variables of INTERFACE type with qualifier for every concrete implementation with different reference name. Now, in HTTP request, put boolean value as a decider req param to pick specific injected dependency among all.
        B. using configuration class to create bean dynamically using property value. We will not put @Component over any confusing concrete implementation of interface, but like this in @Bean annotated object supplier method:
            @Bean
            public InterfaceType createInterfaceTypeObject (@Value("${prop.from.appOrInlineLiteralsOrEnvVars}" | "false") boolean boolValue) { if (boolValue) { // pick and return concrete class 1; } else { // pick and return concrete class 2; } }
     */
    @Autowired
    PaymentRepository paymentRepository;

    public PaymentResponse getPaymentDetailsById(PaymentRequest internalRequestObj) {
        PaymentEntity paymentModel = paymentRepository.getPaymentById(internalRequestObj);

        // map it to response obj
        PaymentResponse paymentResponse = mapModelToResponseDTO(paymentModel);
        return paymentResponse;
    }

    private PaymentResponse mapModelToResponseDTO(PaymentEntity paymentEntity) {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(paymentEntity.getId());
        response.setAmount(paymentEntity.getPaymentAmount());
        response.setCurrency(paymentEntity.getPaymentCurrency());
        return response;
    }
}
