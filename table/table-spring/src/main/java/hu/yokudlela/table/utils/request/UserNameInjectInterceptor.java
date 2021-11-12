package hu.yokudlela.table.utils.request;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author krisztian
 */

@Component
public class UserNameInjectInterceptor implements HandlerInterceptor {
    
    @Autowired
    RequestBean user;

    @Override
    public boolean preHandle(HttpServletRequest requestServlet, HttpServletResponse responseServlet, Object handler) throws Exception {        
        try{user.setUser(requestServlet.getUserPrincipal().getName());}
        catch(Exception e){}        
        return true;                        
    }
        
        

}