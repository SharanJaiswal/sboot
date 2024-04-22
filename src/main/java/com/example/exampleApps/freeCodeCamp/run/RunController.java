package com.example.exampleApps.freeCodeCamp.run;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@RestController // If we'll remove this annotation, class won't be able to respond to requests.
@RequestMapping("/api/runs")
public class RunController {

    @GetMapping(value = "/hello")
    String home() {
        return "Hello World";
    }

// Instead of @Autowired, make private final variables of the class to be injected and then inject the spring managed beans of that class using constructor of class where it is injected
    private final RunRepository runRepository;

    public RunController(RunRepository runRepository) { // Here injection is being done by Spring
        this.runRepository = runRepository;
    }

    @GetMapping(value = "")
    List<Run> findAll() {
        return runRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Run findById(@Valid @PathVariable Integer id) {
//        return runRepository.findById(id);
        Optional<Run> run = runRepository.findById(id);
        if (run.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Run Not Found"); // Second arg is optional
            throw new RunNotFoundException();
        }
        return run.get();
    }

    // POST method - for create new bean
    @ResponseStatus(HttpStatus.CREATED) // If this have not been added then default response status is 200. But now we made it 201.
    @PostMapping(value = "")
    void create(@Valid @RequestBody Run run) {
        runRepository.create(run);
    }

    // PUT method - for update
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{id}")
    void update(@Valid @PathVariable Integer id, @Valid @RequestBody Run run) {
        runRepository.update(run, id);
    }

    // DELETE method - for deleting the bean|record
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id}")
    void delete(@Valid @PathVariable Integer id) {
        runRepository.delete(id);
    }

}
