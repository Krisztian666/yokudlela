package hu.yokudlela.table.utils.logging;


import hu.yokudlela.table.utils.request.RequestBean;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import static net.logstash.logback.argument.StructuredArguments.keyValue;



@Component
@Aspect
@Slf4j(topic = "MethodCall")
public class CustomLogger {

    @Autowired
    RequestBean requestScopedBean;    
    
    @Pointcut(value = "@within(hu.yokudlela.table.utils.logging.AspectLogger) && execution(public * *(..))")
    @Order(1)
    public boolean executionOfAnyPublicMethodInAtLoggedType() {return true;}

    @Pointcut(value = "execution(* *(..)) && @annotation(hu.yokudlela.table.utils.logging.AspectLogger)")
    @Order(2)
    public boolean executionOfLoggedMethod() {return true;}

    
    
    @Around("executionOfAnyPublicMethodInAtLoggedType() || executionOfLoggedMethod()")
    public Object logMethodWithAspect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object retVal = proceedingJoinPoint.proceed();
        stopWatch.stop();
            log.info(
                String.format("%s",  
                        getMethodNameWithArgs(proceedingJoinPoint))
                    ,keyValue("methodetime", stopWatch.getTotalTimeMillis())
            );                
        return retVal;
    }

    public String getMethodNameWithArgs(ProceedingJoinPoint proceedingJoinPoint) {
        return String.format("%s(%s)",
                proceedingJoinPoint.getSignature().getName(),
                getMethodArgs(proceedingJoinPoint).stream().map(Object::toString).collect(Collectors.joining(",")));
    }


    private List<Object> getMethodArgs(ProceedingJoinPoint proceedingJoinPoint) {
        return getMethodAnnotation(proceedingJoinPoint, AspectLogger.class).map(aspectLogger -> {
            if (aspectLogger.skipParametersByIndex().length == 0) {
                return Arrays.asList(proceedingJoinPoint.getArgs());
            } else {
                final Object[] args = proceedingJoinPoint.getArgs();
                final int[] skippedArgs = aspectLogger.skipParametersByIndex();
                List<Object> result = new ArrayList<>();
                for (int i = 0; i < args.length; i++) {
                    if (!ArrayUtils.contains(skippedArgs, i)) {
                        result.add(args[i]);
                    }
                }
                return result;
            }
        }).orElseGet(Collections::emptyList);
    }


    private <T extends Annotation> Optional<T> getMethodAnnotation(ProceedingJoinPoint proceedingJoinPoint, Class<T> annotationClass) {
        Signature signature = proceedingJoinPoint.getSignature();
        return signature instanceof MethodSignature ?
                Optional.ofNullable(((MethodSignature) signature).getMethod().getAnnotation(annotationClass))
                :
                Optional.empty();
    }

}
