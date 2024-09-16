package com.example.exampleApps.freeCodeCamp.testCodes;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

// Random order of test method execution. On every test method run, separate instance of test class is created. Hence, no interdependency of test methods via class|obj level attributes.
//@Disabled   // to skip tests
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// overriding default behavior of life of this class object lifecycle from per test method to per class, i.e., object will persist.
// In this way, we can share the class member variables among test methods, but this alone doesn't guarantee the order of test method execution.
// This allows Before|AfterAll methods to be non-static, running them on same instance after this class object gets created and just before destroyed.
@DisplayName("When running sample test class, we can provide messages in continuation of sentences")
public class MathUtilsTest {
    MathUtils mathUtils;

    // Below 2 providers are junit injected by Junits at any place. We can also create are own providers and inject it.
    TestInfo testInfo;
    TestReporter testReporter;

    // lifecycle hooks of junits. BeforeAll - BeforeEach - AfterEach - AfterAll
    @BeforeAll
    static void beforeAllInit() {
        System.out.println("This needs to run before this test class instantiation. But there is a restriction of it being static.");
    }

    @AfterAll
    static void afterAllInit() {
        System.out.println("This needs to run after this test class object is destroyed. But there is a restriction of it being static.");
    }

    @BeforeEach
        // runs just before on execution call of each test methods
    void init(TestInfo testInfo, TestReporter testReporter) {   // this method could be of any name. Also, here is where Junit injects providers.

        // Also, providing these params is optional, both are java interfaces with underlying implementation.
        // First is used to get access info about tests that we need from it. Second is used to pass something to the final report of junit.
        this.testInfo = testInfo;
        this.testReporter = testReporter;

        mathUtils = new MathUtils();
        testReporter.publishEntry("Running " + testInfo.getDisplayName() + " with tags " + testInfo.getTags() + ", and any other implemented methods.");// prints on console of Junit runner, not specifically as sout.
    }

    @AfterEach
        // run just after on execution call of each test methods
    void cleanUp() {    // Could be of any name
        System.out.println("Cleaning Up...");
    }

    // Below annotation allows junit platform to scan all classes and run only those methods which have this annotation, even when we "right-click > run as junit" on parent or top-most package.
    // HACK: add this annotation on any method to let them run on every build
    @Test
    // We need main method to run the method somehow, but since it is not in this test class, or any test class; therefore we provide this annotation. NO FAILURE==SUCCESS
    void test() {
        System.out.println("Not implemented yet");
    }

    @Test
    @Tag("Circle")  // Tagging provides option to group test methods with same tags, so that we can run tests with specific tags when required. Maven has an
    @DisplayName("Testing add method")
        // Good coding practices
    void testAdd1() {
//        MathUtils mathUtils = new MathUtils();
        int expected = 2;
        int actual = mathUtils.add(1, 1);
        assumeTrue(false);
        assertEquals(expected, actual);
    }

    @Test
    @Tag("Circle")
    @EnabledOnOs(OS.LINUX)
    @EnabledOnJre(JRE.OTHER)
        // there are many other conditional executions.
    void testDivide() {
//        MathUtils mathUtils = new MathUtils();
        assertThrows(ArithmeticException.class, () -> mathUtils.divide(2, 0), "Divide by zero throws");

    }

    @Test
    @Tag("Circle")
    @Disabled
        // skips running it
    void disabledTest() {
        System.out.println("TDD test method should not run");
        fail("TDD test method should not run...");
    }

    @Test
    @Tag("Circle")
    void testAssertAll() {
        System.out.println("Running " + testInfo.getDisplayName() + " with tags " + testInfo.getTags() + ", and any other implemented methods.");
        testReporter.publishEntry("Running " + testInfo.getDisplayName() + " with tags " + testInfo.getTags() + ", and any other implemented methods.");// prints on console of Junit runner, not specifically as sout.
        assertAll(
                () -> assertEquals("a", "a")
                , () -> assertEquals(1, 1)
                , () -> assertEquals('c', 'c')
//                ,() -> assertEquals('c', 'd')
        );
    }

    @Nested // Grouping the test cases, could be for different reason. If any one fails, all assumed to be failed.
    class AddTest {
        @Test
        @Tag("Circle")
        void test() {
            System.out.println("Not implemented yet");
        }

        @Test
        @Tag("Math")
        @DisplayName("Testing add method")
            // Good coding practices
        void testAdd1() {
//        MathUtils mathUtils = new MathUtils();
            int expected = 2;
            int actual = mathUtils.add(1, 1);
            assumeTrue(false);
            assertEquals(expected, actual);
        }
    }

    @Test
    @Tag("Math")
    @DisplayName("Testing add method 2")
        // Good coding practices
    void testAdd2() {
//        MathUtils mathUtils = new MathUtils();
        int expected = 2;
        int actual = mathUtils.add(1, 1);
// Passing string is expensive because its creating and computation happens even before assertion method is called.
// If our requirement is to only display statement when test method fails, then pass the lambda.
// This will only execute lambda when assertion fails, eliminating cost of string message.
        assertEquals(expected, actual, () -> "should return sum " + expected + " but returned the sum " + actual + ".");
    }

    @RepeatedTest(3)
    @Tag("Math")
        // Repeats same test N number of times. Similar to Nested, where if one fails, all fail.
    void sampleTest1(RepetitionInfo repetitionInfo) {   // Junit provide a way to get information of the iteration, only when there is @RepeatedTest(N) annotation.
        if (repetitionInfo.getCurrentRepetition() <= repetitionInfo.getTotalRepetitions()) {
            assertEquals(1, 1);
        }
    }
}
