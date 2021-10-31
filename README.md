# Yokudlela
Java Microservice projekt example.

## Task
- Validáld a beérkező adatokat
- https://youtu.be/oJO53Ua8PgQ

## Important information
- https://docs.jboss.org/hibernate/beanvalidation/spec/2.0/api/index.html?javax/validation/package-summary.html

## Maven
```
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-validation</artifactId>
   <version>2.3.7.RELEASE</version>
</dependency>
               
<dependency>
   <groupId>jakarta.validation</groupId>
   <artifactId>jakarta.validation-api</artifactId>
   <version>2.0.2</version>
</dependency>
```


### Spring
- @Validated
- @ControllerAdvice
- @ExceptionHandle
- ResponseEntityExceptionHandler

### Validation API
- @AssertFalse
- @AssertFalse.List
- @AssertTrue
- @AssertTrue.List
- @DecimalMax
- @DecimalMax.List
- @DecimalMin
- @DecimalMin.List
- @Digits
- @Digits.List
- @Email
- @Email.List
- @Future
- @Future.List
- @FutureOrPresent
- @FutureOrPresent.List
- @Max
- @Max.List
- @Min
- @Min.List
- @Negative
- @Negative.List
- @NegativeOrZero
- @NegativeOrZero.List
- @NotBlank
- @NotBlank.List
- @NotEmpty
- @NotEmpty.List
- @NotNull
- @NotNull.List
- @Null
- @Null.List
- @Past
- @Past.List
- @PastOrPresent
- @PastOrPresent.List
- @Pattern
- @Pattern.List
- @Positive
- @Positive.List
- @PositiveOrZero
- @PositiveOrZero.List
- @Size
- @Size.List
