package hu.yokudlela.media.rabbit;

import hu.yokudlela.table.utils.request.RequestBean;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author (K)risztian
 */
@Data
@NoArgsConstructor
public class QueueModel {
    
    private String base64data;
 

    @Builder
    public QueueModel(String base64data) {
        this.base64data = base64data;
    }
    
    


}
