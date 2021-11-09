package hu.yokudlela.table.utils.validation;

import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author (K)risztian
 */
@Data
@NoArgsConstructor
public class ApiError {
    
   private String path;
    
   private String message;
   
   private Set<String> errors = new LinkedHashSet<>() ;


   @Builder
    public ApiError(String path, String message) {
        this.path = path;
        this.message = message;
    }

    
}
