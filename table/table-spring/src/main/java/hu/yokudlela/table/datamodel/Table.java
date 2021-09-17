
package hu.yokudlela.table.datamodel;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Asztal")
public class Table {
    @Schema(description = "Asztal elnevezése")
    private String name;
    @Schema(description = "Aktuálisan használható?l")
    private boolean available = true;
    @Schema(description = "Székek száma az asztalnál")
    private byte capacity;
    @Schema(description = "Fényképek")
    private List<String> imageurls= new ArrayList<>();

    @Builder
    public Table(String name, byte capacity) {
        this.name = name;
        this.capacity = capacity;
    }
    
    
}
