/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.yokudlela.table.utils.request;

/**
 *
 * @author oe
 */
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;
import org.springframework.util.StopWatch;

/**
 * A filter to create transaction before and commit it once request completes
 * The current implemenatation is just for demo
 *
 * @author hemant
 *
 */
@Component
@Order(1)
@Slf4j
public class RequestFilter implements Filter {

    public static final String REQUEST_ID = "requestId";
    public static final String USER_ID = "userId";
    public static final String NOT_DEF = "--N/A--";
    public static final String TIME_SPENT = "timeSpentMS";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        HttpServletRequest req = (HttpServletRequest) request;
        request.setAttribute(REQUEST_ID, (req.getHeader(REQUEST_ID)!=null)?req.getHeader(REQUEST_ID):UUID.randomUUID().toString());
        request.setAttribute(USER_ID, (req.getUserPrincipal() != null) ? req.getUserPrincipal().getName() : NOT_DEF);

        chain.doFilter(request, response);
        stopWatch.stop();
        log.info("", 
                StructuredArguments.keyValue("state","end of request"),
                StructuredArguments.keyValue("uri",req.getRequestURI()),
                StructuredArguments.keyValue("url",req.getRequestURL()),
                StructuredArguments.keyValue(REQUEST_ID, request.getAttribute(REQUEST_ID)),
                StructuredArguments.keyValue(USER_ID, request.getAttribute(USER_ID)),
                StructuredArguments.keyValue(TIME_SPENT, stopWatch.getTotalTimeMillis())
        );
    }

}
