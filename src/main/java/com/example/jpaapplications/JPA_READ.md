 Application Logic ===X=== ORM Framework ===X=== JDBC ===X=== Specific DB Driver  ===X=== Relational DB
                          JPA+Hibernate         Interface       Implementation
                    Interface+Implementation
 
::::JDBC::::
make conn to DB; Query DB; process results; is implemented by DB specific drivers.

::::Drivers::::
Mysql - Connector/J     ::::     Class:: com.mysql.cj.jdbc.Driver
PosgresSQL - PostgresSQL JDBC Driver    ::::  Class:: org.postgresql.Driver
H2 - H2 Database Engine     ::::    Class      :: org.h2.Driver




FOR JPA Implementation:
1. in pom.xml we have spring-boot-starter-data-jpa  -  this internally bring hibernate dependency also, so no explicit mention of hibernate dependency
 - in pom.xml we have com.h2database . h2 at runtime
2. in properties file
   spring.datasource.url=jdbc:h2:mem:userDB
   spring.datasource.driver-class-name=org.h2.driver
   spring.datasource.username=sa
   spring.datasource.password=
   
   # OPTIONAL PROPS:
   spring.h2.console.enabled=true
   spring.h2.console.path=/h2-console
   # OPTIONAL - ONLY IF WE WANT TO SHOW WHAT QUERIES ARE RUNNING INTERNALLY:
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.format_sql=true
   # Optional - Define packages to scan: Although SB scans for all classes with @Entity
   spring.jpa.packages-to-scan=com.company.package
   # Optional for prop file, ie 1 DB config - Define provider
   spring.jpa.properties.javax.persistence.provider=org.hibernate.jpa.HibernatePersistenceProvider (default value)
   spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
   # Transaction type - Default is RESOURCE_LOCAL (for interacting with only 1 DB throughout the application), else JTA (Java Transaction API)
   spring.jpa.properties.javax.persistence.transactionType=RESOURCE_LOCAL
   # Tells hibernate regarding how to create and manage the DB schema
   spring.jpa.hibernate.ddl-auto=
   none:NOT create or update or delete schema. Good for Production env.
   update: create if absent, and update schema without deleting any data or schema. Not delete schema or data. Good for dev env. eg. adding cols, table, etc.
   validate: NOT create or update OR delete. During app startup, does matching between entities and DB schema. If mismatch found, throws exception.
   create: create,update,delete schema, by dropping and recreating schema during app startup. Data and schema gets deleted.
   create-drop: create,update,delete schema during startup and drops the schema when app shutdowns. (DEFAULT FOR in-MEM H2 DB). Dtaa and schema gets deleted.


3. Any class with @Entity represents the table in the DB, has getters and setters, a default public constructor, an optional public constructors without @Id @GeneratedValue(strategy = GenerationType.AUTO|IDENTITY), all fields as private. Everytime an object is created represents a row of/for a table.
4. Another interface with @Repository for a given table extending the JpaRepository<EntityClassName, KeyDataType> {} JpaRepo provides APIs for insert, get, delete, modify, save, etc. the table. Apart from these abstracted queries, if we want to own JPQL then in this interface we can define new methods for those JPQLs.

============================================================================================================
============================================================================================================

If we want to connect to only one DB for this application, then all the configurations can be mentioned inside properties file. Otherwise, for more than 1 DB connections, we must define a configuration class.

============================================================================================================
=================================================  JPA Architecture  =======================================

"Persistence Unit1" is used to create "EntityManagerFactory1" at app startup, having 1:1 relation between them. Persistence Unit is where we provide DB configurations like connections,dialect,driver,etc. 1 persistence unit is for 1 DB config, and hence 1 DB config is used to make 1 java object of EntityManagerFactory. So, M numbers of DBs in apps => M numbers of PersistenceUnits(DB configs) and M number of EntityManagerFactory Object.
Persistence Unit is hence logical grouping of entity classes which share same configurations, like db connections, JPA providers(hibernate, EclipseLink, OpenJPA, etc.), etc.
EntityManagerFactory is picks default property value if property is not provided and set in EntityManagerFactory.
"EntityManagerFactory1" class is used to create object of "EntityManager 1 ... N" having 1:Many relation between them.
Entity Manager is nothing but an interface in JPA which provides us APIs which is used to interact with the underlying DB for that entity manager object, using methods like persist,merge,find,remove,createQuery(for JPQL). (lets say, APIs from JpaRepository which servers as a wrapper for the APIs for actual APIs from Entity Manager. Strictly, JPA APIs => Hibernate impl => Entity Manager APIs).
EntityManager interface methods are implemented by JPA Vendors like Hibernate etc. Each "Entity Manager x" manages "Persistence Context x", having 1:1 relation. So, same count of Persistence Context is also present as of Entity Manager.
PersistenceContext holds and manages lifecycle of list of entities "Entity 1 ... N" together whic is working on, having 1:Many relationship. This is connected to Dialect which translates JPQL to JDBC comprehensible SQL, which is picked by JDBC/Driver, which is connected to DB. Consider PC as first level cache. 
EntityManager insert.update/de;ete operations are transaction bounded. Means, it first checks if transaction is open. If not, it throws exception. Not All READ operations ae transaction bounded.

============================Transaction Manager association with EntityManagerFactory======================
Default is Resource-local, where for each persistence unit (PU, i.e., for each DB), there is 1 EMF and hence for each EMF there is 1 transaction Manager - TM.
But, if we want single TM for multiple PU and hence multiple EM setup, to manage multiple DBs, then we explicitly configure JTA.

=================================================================================================================================
DB and Schema: Generally, well most of the time, logical grouping of tables forms a schema. There can be several schemas in a DB. Schemas can be isolated from each other.
=================================================================================================================================
@Table(name="TABLE_NAME", schema="ONBOARDING") 
Table annotation is optional. If this is absent, then hibernate will consider the @Entity class TableName in CamelCase to comprehend it as TABLE_NAME uppercase_snakecase in DB.
For optional schema attribute, if we want out table to be part of any particular schema, then we need to mention that schema. Hibernate doesn't by default creates or put our table in any schema.
The way to create schema(S): spring.datasource.url=jdbc:h2:mem:userDB;INIT=CREATE SCHEMA IF NOT EXISTS ONBOARDING1[;CREATE SCHEMA IF NOT EXISTS ONBOARDING2;...]. If table is not attributed with schema even though there is specific schema in the DB, then this entity table will not be part of any schema.
@Table(uniqueConstraints={
      @UniqueConstraint(columnNames="phones"), // single col unique constraint
      @UniqueConstraint(columnNames={"name","email"}), // composite col unique constraint
      }, // select * from information_schema.constraint_column_usage
      indexes={
      @Index(name="index_phone", columnList"phone"),
      @Index(name="index_name_email", columnList="name,email")
      }  // select * from information_schema.index_columns
)

Optional Column annotation over Entity class fields
@Column(name="db_col_name", unique=true, nullable=false, length=255)

=================================================================================================================================
Composite Key: There are 2 ways for it, each with a set of 2 annotations. But for the common part, prepare new class of fields forming composite PK.
That public class should implement Serializable interface, having noArgsConstructor (a default constructor), and must override equals and hashCode methods. hashCode and equals overridden because for JPA needs hashMap to maintain first and second level caching for which key(generally primary key) is required. SERIALIZABLE because of serialization reasons and god knows why.

SELECT * FROM INFORMATION_SCHEMA.INDEX_COLUMNS;

METHOD-1::  Define that new class. Annotate that Entity class with @IdClass(CompositeClassName.class). Annotate all those fields of entity class which has same name as all fields of composite key class with @Id. In this case, an entity class has more than 1 col with@Id annotation.
METHOD-2::  We put @Embeddable over composite class. So, in Entity class, we mention the field of type composite key annotated with @EmbeddedId , and not like above with each field of composite key annotated with @Id.

=================================================================================================================================
=================================================================================================================================
ONE-2-ONE UNIDIRECTIONAL:: One instance of entity A can only reference to one instance of another entity B. B to A is not established.
Here, in A, a field of type of class B (YES THE WHOLE CLASS B) is annotated with @OneToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY) serving as a FK. Name of this field is in camelCase in this class, but in DB it will be in a snake case class_b_name_class_id_field, ie, underscore separated B class name and B's referenced col|key.
To override this default naming: @JoinColumn(name="name_in_db_of_A", referencedColumnName="key_name_from_B_in_DB").
In case of referring composite key: @JoinColumns({ @JoinColumn(name="col_name_for_A_in_DB", referencedColumnName="referenced_col_name_of_comp_key"), @JoinClumn(name="col_name_for_A_in_DB", referencedColumnName="referenced_col_name_of_comp_key")})

CASCADE TYPE::: without cascade type, any operation on PARENT(table with FK) do not affect child(tab which is referred) entity. Also, managing child entity can be error-prone. WE CAN GIVE COMMA SEPARATED BELOW VALUES for "cascade" as cascade=CascadeType.PERSIST, CascadeType.MERGE .
PERSIST: inserting entity in parent automatically inserts associated FK value in referred(child) table. So, 
MERGE: for updating {Design type: use same controller as for insert, but decide for JPA API on the basis of null check of FK field value in request}
REMOVE: for deleting row(s)
BELOW 2 ARE NOT GENERALLY USED
REFRESH: Generally when we query or do some sql operation, the operation most of the times happens in persistence context and not directly from DB. Possibly, data could have been updated in the DB all these while. But REFRESH makes sure that all the parent and child both get operated in the DB itself and not just limited to persistence context.
DETACH: We know, an entity when in persistence context then its lifecycle is fully managed by PC. But on using this detach, we tell JPA to detach Parent and its Child entities as well.
ALL: for all of the above

We saw operation of cascade type for insert, update, and remove. But what about GET call of entity? Does child entities always get loaded(FROM DB) when we call Parent Entity? YES and NO both.
Yes: Default for OneToOne and ManyToOne : EAGER loading
No: LAZY loading. child field only get loaded when we explicitly call parentField.getChildField() from java code. Default for OneToMany and ManyToMany

Error happens due to this is generally of type Serializable error by jackson. Generally due to absence of data of child. We can use @JsonIgone over FK(child entity). But this comes with cost, this will simply ignore the data to map to the parent entity bean even if child entity data is present, or in case of Eager initialization as well. Use DTO for child and parent to send response, and if needed child entity data then only populate the child dto and map it in parent dto.
=================================================================================================================================
=================================================================================================================================
ONE-2-ONE BIDI:: Along with parent having reference of child in entity class, as above, child will also have reference of parent in entity class. But that does not mean at all that in DB there will be cyclic key reference. This will not create any FK in table.
In child entity class, add one field of type Parent entity and annotate it with @OneToOne(mappedBy="attr_name_of_this_childTypeEntity_in_parent_entity", fetchType=...)//fetch is optional
But on fetching the child entity, this will create infinite loop due to bidi mapping. Solution: @JsonManagedReference {over child field in parent entity} and @JsonBackReference {over parent field in child entity}. This will not let jackson go to parent from child for serializing the result, hence breaking the chain of recursion. But, this will not at all include corresponding parent entity data in response. Not even once.
To include parent entity response as well during child fetch, for once, we annotate both child and parent class with @JsonIdentityInfo which ensures that jackson gives unique ID to entity based on property field, so to skip its serialization again recursive chain.
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="unique_field_name_in_this_entity")
=================================================================================================================================
=================================================================================================================================
Owing Side: Table which contains the FK. Inverse Side: Table which does not contain FK.

ONE-2-MANY UNIDIRECTIONAL:: a parent can have many(N) child references. So, either (N) rows in parent will be present with same parent entity but differ only by unique identifier of associated children. Or, as default approach, we make a separate 3rd table where we maintain just mapping of unique identifier of parent and child. Or, we maintain the associated parent's unique identifier in child's table for each child row, but this will also create duplicate child entity data to some extent but still it is optimal approach to some extent.
It is LAZY loaded by default. Do below in parent entity
@OneToMany(caascade.ALL, ..., orphanRemoval=false(DEFAULT)) private List<ChildType> child_field_name = new ArrayList<>();

3rd approach instead of creating new table: in just parent entity, annotate the child entity field with : @JoinColumn(name="fk_name_in_child_to_beForcefully_created_due_to_this", referencedColumn="parent_entity_unique_row_identifier_field")
Here, in 3rd case, owing side will be the child table as it has FK referencing rows of parent.

In orphanRemoval, the child rows which are not referencing the parent row, will be deleted automatically. By default this functionality is off(false), but we can turn it on(true).
=================================================================================================================================
=================================================================================================================================
ONE-2-MANY BIDI::MANY-2-ONE BIDI::: @ManyToOne @JoinColumn(name="...", referencedColumnName="..."") in the owing side, over Parent side field in child field. FK will be created in child table.
Also, dont forget that when we insert data from Parent table to child in OneToMany column case, we internally use method in Parent entity
setChildEntityName(List<ChildEntityName> child_arg) { this.child_arg = child_arg;
// This is imp to maintain and populate the FK in child table referencing the parent's unique row identifier
for (ChildEntity childRow: ) { child_arg.setParentEntity(this); }}
=================================================================================================================================
=================================================================================================================================
MANY-2-ONE UNIDI:: In parent there will be no mention of child. But in Child entity, @ManyToOne @JoinColumn will be annotated over ParentType field, thus creating the FK in child only again.
Its bidi is exactly same as 1-2-many bidi.
=================================================================================================================================
=================================================================================================================================
MANY-2-MANY UNIDI:: There is no Parent-Child concept. Anyone can be assumed any based on business logic. There is always a new table is created to maintain relationship mapping between 2 tables.
Since it is unidi, so in one of the table, there won't be any reference of that another table, and it'll have just fields of its own. However, in another table, there will be a field if type private List<FirstTable> fieldName=new ArrayList<>();
It'll be annotated with:
@ManyToMany @JoinTable(
name="parent_child_new_tabel_name", joinColumns=@JoinColumn(name="parent_id_as_fk_for_referencing_this_table"), inverseJoinColumns=@JoinColumn(name="child_id_FK_name_referencing_another_table")
)
=================================================================================================================================
=================================================================================================================================
MANY-2-MANY BIDI:: First table will be same as above will, while this time the another table will have a field of type List<FirstTable>=new ArrayList<>(), annotated with @ManyToMany(mappedBy="filed_name_of_list_of_this_another_table_type_in_first_table")
Write in setters of this another table, the logic to update the foreign key.