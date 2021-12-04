package hu.yokudlela.table.utils.logging;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

/**
 * @author Krisztian
 */
@Component
@Order(1)
@Slf4j
public class CustomRequestLoggingFilter extends AbstractRequestLoggingFilter {

    public static final String REQUEST_ID = "requestId";
    public static final String USER_ID = "userId";
    public static final String NOT_DEF = "--N/A--";
    public static final String TIME_SPENT = "timeSpentMS";

    public CustomRequestLoggingFilter() {
        super();
        this.setIncludeClientInfo(true);
        this.setIncludeQueryString(true);
        this.setIncludePayload(true);
        this.setIncludeHeaders(true);
        this.setMaxPayloadLength(64000);

    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {

        request.setAttribute(REQUEST_ID, (request.getHeader(REQUEST_ID) != null) ? request.getHeader(REQUEST_ID) : UUID.randomUUID().toString());
        request.setAttribute(USER_ID, (request.getUserPrincipal() != null) ? request.getUserPrincipal().getName() : NOT_DEF);

        log.info(message,
                StructuredArguments.keyValue(REQUEST_ID, request.getAttribute(REQUEST_ID)),
                StructuredArguments.keyValue(USER_ID, request.getAttribute(USER_ID))
        );
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
    }

}
