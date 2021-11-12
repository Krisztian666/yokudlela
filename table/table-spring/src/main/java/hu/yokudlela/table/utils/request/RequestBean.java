package hu.yokudlela.table.utils.request;

import java.util.UUID;
import lombok.Data;

/**
 * @author user
 */
@Data
public class RequestBean {

    private String uuid = UUID.randomUUID().toString();
    
    private String user;

    public RequestBean() {
        uuid = UUID.randomUUID().toString();
    }
        
    public RequestBean(String user) {
        uuid = UUID.randomUUID().toString();
        this.user = user;
    }
            
}