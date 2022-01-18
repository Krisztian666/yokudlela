# Yokudlela
Java Microservice projekt example.

## Task
- Kapcsoljunk össze szolgáltatásokat asszinron módon egy RabbitMQ segítségével.
https://youtu.be/1PFNQPi0mPE

## Important information
 
## Maven
```
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>
```
### Rabbit
- RabbitTemplate
- TopicExchange
- Queue
- Binding
- BindingBuilder
- MessageListener
- @RabbitListener
- Message

### JSON serialization
- Jackson2JsonMessageConverter
