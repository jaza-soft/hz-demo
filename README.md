# Hibernate L2 Cache using Hazelcast

## Setup
- include dependencies
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <dependency>
        <groupId>com.hazelcast</groupId>
        <artifactId>hazelcast</artifactId>
    </dependency>

    <dependency>
        <groupId>com.hazelcast</groupId>
        <artifactId>hazelcast-hibernate53</artifactId>
    </dependency>
</dependencies>
```
- configure application.yml
```yaml
spring:
  jpa:
    properties:
      hibernate:
        cache:
          use_second_level_cache: true
          use_query_cache: true
          region:
            factory_class: com.hazelcast.hibernate.HazelcastCacheRegionFactory
```
- add `@org.hibernate.annotations.Cache` annotation on entity of L2C
```java
@Entity
@Cache(region = "book", usage = CacheConcurrencyStrategy.READ_WRITE)
public class Book {}
```
- add `@org.springframework.data.jpa.repository.QueryHints` annotation on Repository methods for Query Cache
```java
@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
  @Override
  @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
  Iterable<Book> findAll();
}
```

## Standalone entity

**L2 Cache**

- Only adding `@org.hibernate.annotations.Cache` annotation is enough. No additional config required.
- Hibernate handles invalidation properly as per expectation
  - On Update cache is invalidated and new value is cached
  - On Delete cache is invalidated

**Query Cache**

- Need to add `@org.springframework.data.jpa.repository.QueryHints` annotation on Repository methods for which cache needs to enabled
- Hibernate invalidates cache even if there are any changes in current table as well as other table
