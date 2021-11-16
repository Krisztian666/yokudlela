package hu.yokudlela.table.utils.request;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import static hu.yokudlela.table.utils.logging.CustomRequestLoggingFilter.REQUEST_ID;
import static hu.yokudlela.table.utils.logging.CustomRequestLoggingFilter.USER_ID;

/**
 * @author krisztian
 */

@Component
@Slf4j
public class UserNameInjectInterceptor implements HandlerInterceptor {
    
    @Autowired
    RequestBean user;
    

    @Override
    public boolean preHandle(HttpServletRequest requestServlet, HttpServletResponse response, Object handler) throws Exception {        
       
        try{
            user.setRequestId(""+requestServlet.getAttribute(REQUEST_ID));
            user.setUser(""+requestServlet.getAttribute(USER_ID));
        }
        catch(Exception e){log.error(e.getLocalizedMessage(), e);}     
        
        return true;                        
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        
        
    }
        
    

}