package com.example.exampleApps.freeCodeCamp.user;
/**
 * In general, to create a immutable POJO of a class, we need to make class as final(so that it cannot be subclassed), and its fields as private, with no setter methods.
 * So a record is like a data-carrying POJO "final class" without setter methods(immutable class object). It extends java.lang.Record.class on running getSuperClass().
 * Hence, we cannot extend but implement another classes.
 * It has implicit public all args constructor, and public getter methods with name same as fields. This constructor's body can be overridden as public. In addition, few other constructors can be added with different params but they should call canonical constructor so that all components are initialized.
 * We can override these getters also, along with their access specifiers. We can add additional methods. We can override other methods of class like hashCode,equals,toString
 * We can increase or keep same as record, the constructor access level but cannot decrease it, i.e., cannot make constructors more restrictive than access of record itself.
 * Class params are called "record components" which internally generated as "private final" fields in compiled files. Anything other than these inside record is not allowed other than static fields.
 * We can also write this default canonical constructor(params same as record components) in a compact form as ::: public ClassName {} :::: No need to pass params and inside assignment statements, but we can write own other logic like exception throw which is assumed to be before implicit assignments.
 * Rules for access specifier for one-level (non-nested) record is same as normal class.
 *
 * For nested records, nested records can be of any access-specifier BUT THEY HAVE TO BE STATIC RECORDS ONLY INTERNALLY. Hence can be accessed as "OutRecord.InRecord".
 * We can also add static and non-static class inside record. Static class can be accessed as above. Non-static class's object will be instantiated using parent record object.
 *
 * Local Records: Record inside braces {}: Their scope is only till this block. They cannot be static as their scope is not of class but of {}. For same reason, no need of the concept of access specifiers of local records. Their objects can be created inside the block only.
 */

/**
 * {
 *  "id": 1,
 *  "name": "Leanne Graham",
 *  "username": Sincere@april.biz",
 *  "address": {
 *     "street": "Kulas Light",
 *     "suite": "Apt. 556",
 *     "city": "Gwenborough",
 *     "zipcode": "92998-3874",
 *     "geo": {
 *         "lat": "-37.3159",
 *         "lng": "81.1496"
 *     }
 *   },
 *   "phone": "1-770-736-8031 x56442",
 *   "website": "hildegard.org",
 *   "company": {
 *      "name": "Romaguera-Crona",
 *      "catchPhrase": "Multi-layered client-server",
 *      "bs": "harness real-time e-markets"
 *   }`
 * }
 */
public record User(
        Integer id,
        String name,
        String username,
        String email,
        Address address,
        String phone,
        String website,
        Company company
) {
}
