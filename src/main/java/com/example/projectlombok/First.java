package com.example.projectlombok;
// LOMBOK work during compile time. So, there might be case where while writing code we'll see error, or code will not make sense. But during compilation, lombok codes gets replaced with the actual sensible boilerplate codes.

import lombok.NonNull;
import lombok.ToString;
import lombok.val;

public class First {
    public static void main(String[] args) {
        // 1. Lombok's "val"(final|immutable) and Java's own "var"(mutable|non-final) used as "type" replacement FOR LOCAL VARIABLES ONLY, NOT FOR PARAMS OR FIELDS. Actual type will be inferred from "initializer expression".
        val a = 30;
//        a = 40; // this will give error
        var b = 30;
        b = 40;
    }

    public void demoMethod1(@NonNull String name) {
        System.out.println(name);   // Equivalent to add if else statement where if cond is variable==null then throw NPW("VARIABLE is marked non-null but is null") else this
    }
     // @Getter @Setter but are public methods by default. They are meant for object level fields, nor static|class-level fields. SETTER is not for FINAL also.
    // @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PROTECTED) on fields or class to override the class level default accesslevel of public

    // We can use both at class level as well. Getter will be applied to all non-static fields. Setter will be applied to all non-static and non-final fields.
    // To skip|override few fields from class level getter and setter, use @G|Setter(AccessLevel.NONE) over those fields, while applying them raw on class level also.
}

@ToString   // Generally used with logger statements
class TestPojo {
    String name;
    boolean committeeMember;
}
/**
 * above is equivalent to generating a class with empty unparameterized public constructor, and a method toString where return statement is:
 * ClassName(field1name=field1val, field2name=field2val, ...)
 *
 * But to exclude certain fields from this class level annotation, we use additional @ToString.Exclude annotation over those fields.
 * To skip fieldIname= and just comma separated field values in log statement, to reduce size, we can add @ToString(includeFieldName=false) to class annotation
 *
 * Reverse way: To explicitly include certain fields: @ToString(onlyExplicitlyIncluded=true) over class, and @ToString.Include over fields to include.
 */

/**
 * AllArgsConstructor : All fields except static and final initialized fields
 * NoArgsConstructor : No Args
 * RequiredArgsConstructor : with only final OR @NonNull fields
 */

// @EqualsAndHashCode

/**
 * @Data : includes--:
 * ToString, EqualsAndHashCode, Getter, Setter, RequiredArgsConstructor
 */

/**
 * @Value : Immutable version of @Data
 * All fields are made private and final
 * Setters are not generated as all fields are made final
 * Class itself made final
 * Just like @data annotation, - toString, EqualsAndHashCode methods get generated
 * @Getter are made for all fields as all fields are made final
 * @RequiredArgsConstructor on all fields since all fields are made final and hence this works as AllArgsConstructor
 */

// @Builder
