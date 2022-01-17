package hu.yokudlela.media.spring;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import hu.yokudlela.media.rabbit.Receiver;
import hu.yokudlela.table.utils.request.RequestBean;
import hu.yokudlela.table.utils.request.UserNameInjectInterceptor;
import hu.yokudlela.table.utils.validation.ValidationRestDataExceptionHandler;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.jboss.logging.MDC;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author (K)risztián
 */

@SecurityScheme(
        type = SecuritySchemeType.OAUTH2,
        name = "oauth2",
        description = "KeyCloak Yokudlela",
        flows = @OAuthFlows(
                implicit = @OAuthFlow(authorizationUrl = "http://yokudlela:6080/auth/realms/yokudlela/protocol/openid-connect/auth"
                        + "?client_id=account"
                        + "&redirect_uri=http://yokudlela:8060/media/swagger-ui/oauth2-redirect.html"
                        + "&response_type=code"
                        + "&scope=openid")
        )
)

@SecurityScheme(
        type = SecuritySchemeType.APIKEY,
        name = "apikey",
        paramName = "Authorization",
        description = "KeyCloak Yokudlela",
        in = SecuritySchemeIn.HEADER)

@SecurityScheme(
        type = SecuritySchemeType.OPENIDCONNECT,
        name = "openid",
        description = "KeyCloak Yokudlela",
        openIdConnectUrl = "http://yokudlela:6080/auth/realms/yokudlela/.well-known/openid-configuration"
)

@OpenAPIDefinition(
        servers = {
            @Server(url = "http://yokudlela:8060/media", description = "local dev")},
        info = @Info(
                title = "Yokudlela Medi API",
                version = "v1",
                description = "description = \"Yokudlela Table API for Graphical User Interface .",
                license = @License(
                        name = "Custom 4D Soft",
                        url = "https://www.4dsoft.hu"),
                contact = @Contact(
                        url = "https://www.4dsoft.hu",
                        name = "Karóczkai Krisztián", email = "krisztian_karoczkai@4dsoft.hu")))

@Configuration
@EnableWebMvc
@EnableAutoConfiguration
@ComponentScan(basePackages = {
    "hu.yokudlela.table.service",
    "hu.yokudlela.media.rest",
    "hu.yokudlela.media.rabbit",
    "hu.yokudlela.table.utils.request",
    "hu.yokudlela.table.utils.logging"
})
@EnableJpaRepositories("hu.yokudlela.table.store")
@EntityScan("hu.yokudlela.table.datamodel")
@EnableCaching
@SpringBootApplication
@Import(ValidationRestDataExceptionHandler.class)
public class MediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediaApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Autowired
            UserNameInjectInterceptor customInterceptor;

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(customInterceptor);
            }

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }

    @Bean("requestScopedBean")
    @Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public RequestBean requestBean() {
        MDC.put("application", "2");
        MDC.put("host", "3");
        return new RequestBean();
    }

    @Bean("applicationContextProvider")
    public ApplicationContextProvider createApplicationContextProvider() {

        return new ApplicationContextProvider();
    }
    
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(100000);
        return multipartResolver;
    }
    
    private static final String mqExchange = "menuadmin";
    private static final String mqQueue = "menuadmin.add";
    
    @Bean
    Queue queue() {
        return new Queue(mqQueue, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(mqExchange);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
    }
    
    
    @Bean
    Jackson2JsonMessageConverter rabbitMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    
}
