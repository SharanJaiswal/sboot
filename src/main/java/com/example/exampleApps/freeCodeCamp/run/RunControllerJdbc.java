package com.example.exampleApps.freeCodeCamp.run;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/h2jdbc")
public class RunControllerJdbc {
    private final RunRepositoryJdbc runRepositoryJdbc;

    public RunControllerJdbc (RunRepositoryJdbc runRepositoryJdbc) {
        this.runRepositoryJdbc = runRepositoryJdbc;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/getAllRecords")
    public List<Run> findAll() {
        return runRepositoryJdbc.findAll();
    }

    @GetMapping(value = "/findById/{id}")
    public Run findById(@PathVariable Integer id) {
        Optional<Run> run = runRepositoryJdbc.findById(id);
        if (run.isEmpty()) {
            throw new RunNotFoundException();
        }
        return run.get();
    }

    @PostMapping(value = "/createSingleRow")
    public void create (@RequestBody Run run) {
        runRepositoryJdbc.create(run);
    }

    @PutMapping(value = "/updateRow")
    public void update (@RequestBody Run run) {
        runRepositoryJdbc.update(run, run.id());
    }

    @DeleteMapping(value = "/deleteRecord/{id}")
    public void delete (@PathVariable Integer id) {
        runRepositoryJdbc.delete(id);
    }

    @GetMapping(value =  "/totalRecords")
    public int countTotalRecord () {
        return runRepositoryJdbc.count();
    }

    @PostMapping(value = "/createMultipleRows")
    public void saveAll (@RequestBody List<Run> runs) {
        runRepositoryJdbc.saveAll(runs);
    }

    @GetMapping(value = "/findByLocation/{location}")
    public List<Run> findByLocation (@PathVariable String location) {
        return runRepositoryJdbc.findByLocation(location);
    }

}
