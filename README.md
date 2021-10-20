# Yokudlela
Java Microservice projekt example.

## Task
REST Interface

## Tools


## Important information
- https://tools.ietf.org/html/rfc2616
- https://www.baeldung.com/building-a-restful-web-service-with-spring-and-java-based-configuration
- https://www.baeldung.com/spring-rest-openapi-documentation
- https://www.baeldung.com/spring-cors
- https://spring.io/quickstart


## Maven
```
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-ui</artifactId>
  <version>1.5.10</version>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
  <version>2.5.4</version>
</dependency>        
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-core</artifactId>
  <version>2.12.5</version>
</dependency>
```

### Spring
- @Configuration
- @EnableWebMvc
- @EnableAutoConfiguration
- @ComponentScan(basePackages = "hu.yokudlela.table")
- @SpringBootApplication
- @Bean
- @Service
- @Autowired
- @RestController
- @RequestMapping
- @GetMapping
- @PostMapping
- @PutMapping
- @DeleteMapping
- @RequestParam
- @PathVariable
- @RequestBody
- WebMvcConfigurer

### OpenAPI
- @ApiResponses
- @ApiResponse
- @Content
- @Schema
- @Operation
- @Parameter

### JSON
- @JsonDeserialize
- @JsonSerialize
- JsonSerializer    
- LocalDateTimeDeserializer
- LocalDateTimeSerializer    
