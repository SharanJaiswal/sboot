package com.example.exampleApps.freeCodeCamp.run;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.TypeReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class RunJsonDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(RunJsonDataLoader.class);
    private final RunRepositoryJdbc runRepositoryJdbc;
    private final ObjectMapper objectMapper;

    public RunJsonDataLoader(RunRepositoryJdbc runRepositoryJdbc, ObjectMapper objectMapper) {
        this.runRepositoryJdbc = runRepositoryJdbc;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        if (true) { // runRepositoryJdbc.count() == 0
            try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/runs.json")) {
                Runs allRuns = objectMapper.readValue(inputStream, Runs.class);
                log.info("Reading {} runs from JSON data and saving to in-memory collections.", allRuns.runs().size());
                runRepositoryJdbc.saveAll(allRuns.runs());
            } catch (IOException e) {
                throw new RuntimeException("Not Loading Runs from JSON data because the collection contains data.");
            }
        }
    }
}
