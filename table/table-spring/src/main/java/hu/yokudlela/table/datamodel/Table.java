
package hu.yokudlela.table.datamodel;
import java.util.List;
import java.util.ArrayList;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Egy asztal
 * @author (K)risztián
 */
@Data
@EqualsAndHashCode()
@NoArgsConstructor
public class Table {
    private String name;
    private boolean available = true;
    private byte capacity;
    private List<String> imageurls= new ArrayList<>();

    @Builder
    public Table(String name, byte capacity) {
        this.name = name;
        this.capacity = capacity;
    }
    
    
}
