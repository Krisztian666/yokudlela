package hu.yokudlela.table.utils.request;

import java.util.UUID;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author user
 */
@Data
@Slf4j
public class RequestBean {

    private String requestId ;
    
    private String user;
    
    private String token;
    
    private String client;

}