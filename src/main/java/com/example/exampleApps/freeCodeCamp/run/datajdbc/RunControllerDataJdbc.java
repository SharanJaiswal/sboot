package com.example.exampleApps.freeCodeCamp.run.datajdbc;

import com.example.exampleApps.freeCodeCamp.run.Run;
import com.example.exampleApps.freeCodeCamp.run.RunNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // If we'll remove this annotation, class won't be able to respond to requests.
@RequestMapping("/datajdbc")
public class RunControllerDataJdbc {
    private final RunRepositoryDataJdbc runRepositoryDataJdbc;

    public RunControllerDataJdbc(RunRepositoryDataJdbc runRepositoryDataJdbc) { // Here injection is being done by Spring
        this.runRepositoryDataJdbc = runRepositoryDataJdbc;
    }

    @GetMapping(value = "")
    List<Run> findAll() {
        return runRepositoryDataJdbc.findAll();
    }

    @GetMapping(value = "/{id}")
    public Run findById(@Valid @PathVariable Integer id) {
//        return runRepository.findById(id);
        Optional<Run> run = runRepositoryDataJdbc.findById(id);
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
        runRepositoryDataJdbc.save(run);
    }

    // PUT method - for update
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{id}")
    void update(@Valid @PathVariable Integer id, @Valid @RequestBody Run run) {
        runRepositoryDataJdbc.save(run);
    }

    // DELETE method - for deleting the bean|record
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id}")
    void delete(@Valid @PathVariable Integer id) {
        runRepositoryDataJdbc.deleteById(id);
    }

    @GetMapping(value = "/findByLocation/{location}")
    public List<Run> findByLocation (@PathVariable String location) {
        return runRepositoryDataJdbc.findAllByLocation(location);
    }

}
