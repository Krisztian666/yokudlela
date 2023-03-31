package hu.yokudlela.table.spring;

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
import org.springframework.web.context.WebApplicationContext;
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
                implicit = @OAuthFlow(authorizationUrl = "https://yokudlela.drhealth.cloud/auth/realms/yokudlela/protocol/openid-connect/auth"
                        + "?client_id=account"
                        + "&redirect_uri=https://yokudlela.drhealth.cloud/table/swagger-ui/oauth2-redirect.html"
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
        openIdConnectUrl = "https://yokudlela.drhealth.cloud/auth/realms/yokudlela/.well-known/openid-configuration"
        )

@OpenAPIDefinition(
        servers = {
            @Server(url = "https://yokudlela.drhealth.cloud/table", description = "production"),
			@Server(url = "https://table.yokudlela.drhealth.cloud/table", description = "dev"),
			@Server(url = "https://integration.yokudlela.drhealth.cloud/table", description = "integration"),
			@Server(url = "http://localhost:8080/table", description = "local dev"),
		},

        info = @Info(
                title = "Yokudlela Table API", 
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
    "hu.yokudlela.table.rest",
    "hu.yokudlela.table.utils.request",
    "hu.yokudlela.table.utils.logging"
})
@EnableJpaRepositories("hu.yokudlela.table.store")
@EntityScan("hu.yokudlela.table.datamodel")
@EnableCaching
@SpringBootApplication
@Import(ValidationRestDataExceptionHandler.class)
public class TableApplication {

    public static void main(String[] args) {
        SpringApplication.run(TableApplication.class, args);
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
/*
    @Bean
    public CustomRequestLoggingFilter requestLoggingFilter() {
        CustomRequestLoggingFilter loggingFilter = new CustomRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setIncludeHeaders(true);
        loggingFilter.setMaxPayloadLength(64000);
        
        return loggingFilter;
    }

    @Bean
    public FilterRegistrationBean<RequestFilter> loggingFilter() {
        FilterRegistrationBean<RequestFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new RequestFilter());

        return registrationBean;
    }
*/
}
