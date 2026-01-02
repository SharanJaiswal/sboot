package com.example.exampleApps.freeCodeCamp.run;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.TypeReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component // (value = "ObjectNameForThisClassObject")  // follows "convention over configuration" approach, ie, spring boot will try to autoconfigure based on conventions reducing the need for explicit configuration.
// @Controller, @Service, @Repository etc. all internally tells Spring to create and manage its lifecycle.
// This annotation uses default constructor of class to create its object. If it is not present, then application will fail to start. In such cases, use @Bean and skip @Component here.
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
//            try {
//                InputStream inputStream = new FileInputStream("/data/runs.json"); // Springboot webstarter already has jackson
//                TypeReference<Runs> typeReference = new TypeReference<Runs>() {}; // could be List<Run> also.
//                Runs runs = objectMapper.readValue(inputStream, typeReference);
            try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/runs.json")) {
                Runs allRuns = objectMapper.readValue(inputStream, Runs.class); // Data is loaded here.
                // Inplace of I/PStream==new File("path") . In place of Runs.class, Run[].class, or new TypeReference<List<Run>>(){}
                log.info("Reading {} runs from JSON data and saving to in-memory collections.", allRuns.runs().size());

                // For outputting the values:
                objectMapper.writeValue(new File("/data/output.json"), allRuns);

                runRepositoryJdbc.saveAll(allRuns.runs());
            } catch (IOException e) {
                throw new RuntimeException("Not Loading Runs from JSON data because the collection contains data.");
            }
        }
    }
}
