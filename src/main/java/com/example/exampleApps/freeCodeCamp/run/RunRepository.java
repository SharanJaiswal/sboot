package com.example.exampleApps.freeCodeCamp.run;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class RunRepository {

    // Making EntityManagerFactory available by springboot is done by making its instance created and available using persistence unit.
//    @PersistenceUnit  // This gives the persistence context.
//    private EntityManagerFactory emf;
    // ...and thus, using this, we can create the entity manager object in the code. But there is another way to directly create the Entity Manager object.

//    @PersistenceContext // This directly uses the created shared persistence context object which is risky for multi-threaded persist operations. By default this will throw error on all write operations.
//    @PersistenceContext(type = PersistenceContextType.EXTENDED) // Taking the the authority on self to manage the transactions. Risk is involved for simultaneous write operations.
//    private EntityManager em;

    // The above 2 can be abstracted further, where we directly call the JPA methods. We extend CrudRepository<Class, PK_Type>, and we can use this class's object to directly call JPA methods.


    private List<Run> runs = new ArrayList<>();

    List<Run> findAll() {
        return runs;
    }

//    Run findById(Integer id) {    // This will throw an error if we will give ID other than what is present in record.
//        for (Run run : runs) {
//            if (run.id() == id) {
//                return run;
//            }
//        }
//        return null;

//        return runs.stream().filter(run -> run.id() == id)
//                .findFirst()
//                .get();

//    }

    Optional<Run> findById(Integer id) {
        return runs.stream()
                .filter(run -> Objects.equals(run.id(), id))
                .findFirst();
    }

    void create (Run run) {
        runs.add(run);
    }

    void update(Run run, Integer id) {
        Optional<Run> existingRun = findById(id);
        existingRun.ifPresent(value -> runs.set(runs.indexOf(value), run));
    }

    void delete (Integer id) {
        runs.removeIf(run -> run.id().equals(id));
    }

    @PostConstruct  // Used on method that need to be executed after the dependency injection is done, to perform some initialization. We can use this method to consider doing things just after app starts.
    private void init() {

        runs.add (new Run(1,
                "Monday Morning Run",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                3,
                Location.INDOOR, null));

        runs.add (new Run(2,
                "Wednesday Evening Run",
                LocalDateTime.now(),
                LocalDateTime.now().plus(60, ChronoUnit.MINUTES),
                6,
                Location.INDOOR, null));
    }

}
