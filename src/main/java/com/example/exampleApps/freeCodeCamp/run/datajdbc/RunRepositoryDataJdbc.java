package com.example.exampleApps.freeCodeCamp.run.datajdbc;

import com.example.exampleApps.freeCodeCamp.run.Run;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface RunRepositoryDataJdbc extends ListCrudRepository<Run, Integer> {   // Explore the ListCrudRepository and internally CrudRepository
    // This have all the basic CRUD method. But, if we want to add any extra methods, we can implement it here.

//    @Query("select * .... location = :location")  // We can put @Query annotation as well to run the custom query in the DB server.
    List<Run> findAllByLocation (String location);
}
