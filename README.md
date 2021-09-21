# yokudlela-oe
Java Microservice projekt example.

## Task
REST Interface

## Tools
https://spring.io/quickstart

## Important information
https://tools.ietf.org/html/rfc2616
https://www.baeldung.com/building-a-restful-web-service-with-spring-and-java-based-configuration
https://www.baeldung.com/spring-rest-openapi-documentation
https://www.baeldung.com/spring-cors


### Spring
@Configuration
@EnableWebMvc
@EnableAutoConfiguration
@ComponentScan(basePackages = "hu.yokudlela.table")
@SpringBootApplication
@Bean

@Service
@Autowired
@RestController
@RequestMapping
@GetMapping
@PostMapping
@PutMapping
@DeleteMapping
@RequestParam
@PathVariable
@RequestBody

WebMvcConfigurer

### OpenAPI
@ApiResponses
@ApiResponse
@Content
@Schema
@Operation
@Parameter

### JSON
@JsonDeserialize
@JsonSerialize    
LocalDateTimeDeserializer
LocalDateTimeSerializer
JSONSerializer    
