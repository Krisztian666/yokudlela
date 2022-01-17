package hu.yokudlela.table.utils.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

/**
 * @author Krisztian
 */
@Data
@Slf4j
public class RequestBean {

    private String requestId ;
    
    private String user;
    
    private String token;
    
    private String client;
    
    @JsonIgnore
    private StopWatch watcher;
    
    public RequestBean(){
        this.watcher= new StopWatch();
        this.watcher.start();
    }
    
}