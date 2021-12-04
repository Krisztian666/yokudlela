package hu.yokudlela.media.rest;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Krisztian
 */
@NoArgsConstructor
@Data
public class ImageProcessModel {
    private String id;

    @Builder
    public ImageProcessModel(String id) {
        this.id = id;
    }
    
    
}
