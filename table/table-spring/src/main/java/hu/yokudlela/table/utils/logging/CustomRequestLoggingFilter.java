package hu.yokudlela.table.utils.logging;

import static hu.yokudlela.table.utils.request.RequestFilter.REQUEST_ID;
import static hu.yokudlela.table.utils.request.RequestFilter.USER_ID;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

/**
 * @author Krisztian
 */
@Slf4j
public class CustomRequestLoggingFilter extends AbstractRequestLoggingFilter{

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        log.info(message,
                StructuredArguments.keyValue("state","incoming request"),
                StructuredArguments.keyValue(REQUEST_ID,request.getAttribute(REQUEST_ID)),
                StructuredArguments.keyValue(USER_ID,request.getAttribute(USER_ID))
        ); 
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
    }
    
}
