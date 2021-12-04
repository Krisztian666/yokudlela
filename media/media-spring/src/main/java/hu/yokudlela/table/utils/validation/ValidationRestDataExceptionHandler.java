package hu.yokudlela.table.utils.validation;

import hu.yokudlela.table.service.BusinessException;
import hu.yokudlela.table.utils.request.RequestBean;
import static hu.yokudlela.table.utils.logging.CustomRequestLoggingFilter.REQUEST_ID;
import static hu.yokudlela.table.utils.logging.CustomRequestLoggingFilter.TIME_SPENT;
import static hu.yokudlela.table.utils.logging.CustomRequestLoggingFilter.USER_ID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



/**
 * @author user
 */
@ControllerAdvice
@Component
@Slf4j
public class ValidationRestDataExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    HttpServletRequest req;
    
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> customHandleNotFound(Exception ex, WebRequest request) {
        log.error(ex.getLocalizedMessage(), ex);
       ApiError res = new ApiError(req.getRequestURI(),"error.business");

        res.getErrors().add(ex.getMessage());
        return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);

    }

     @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> HandleConstraintViolation(ConstraintViolationException ex, WebRequest request) {

        log.error(ex.getLocalizedMessage(), ex);
       ApiError res = new ApiError(req.getRequestURI(),"error.validation");
 /*     
       for(Throwable msg: ex.getSuppressed()){
        res.getErrors().add(msg.getLocalizedMessage());
       }
*/         
       for(String msg: ex.getMessage().split(",")){
        res.getErrors().add(msg.split(":")[1]);
       }
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);

    }
    

    
   @ResponseBody
   @Override
   protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                 HttpHeaders headers, HttpStatus status, WebRequest request) {
    log.error(ex.getLocalizedMessage(), ex);
        
       ApiError res = new ApiError(req.getRequestURI(),"error.validation");

        ex.getBindingResult().getFieldErrors().forEach((FieldError error) -> {
            res.getErrors().add(error.getDefaultMessage());
        });                   
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    
    }
    
   
   
   
}
