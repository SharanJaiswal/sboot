package com.example.exampleApps.freeCodeCamp.run;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;

public record Run(
        // Below annotations are implemented by adding Validation dependency
    @Id
    @Positive
    Integer id,

    @NotBlank
    String title,

    @PastOrPresent
    LocalDateTime startedOn,

    @PastOrPresent
    LocalDateTime completedOn,

    @PositiveOrZero
    Integer miles,

//    @NotEmpty
    Location location,

    @Null
    @Version
    Integer version // This will e used to check if it is new row or old row.
) {

// This will be called even before @valid
    public Run {
        if (!completedOn.isAfter(startedOn)) {
            throw new IllegalArgumentException("Completed On must be after Started On!!!");
        }
        // We can add multiple validations
    }

}
/**
 * A record type is something which is immutable. Internally, it has all the methods like equals() hashcode() toString() etc, contructors getters() but not Setters(), for all record components.
 * as they are immutable once its object is created.
 */