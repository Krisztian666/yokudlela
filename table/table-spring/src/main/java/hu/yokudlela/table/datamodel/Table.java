
package hu.yokudlela.table.datamodel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
/**
 * Egy asztal
 * @author (K)risztián
 */
@Data
@EqualsAndHashCode()
@NoArgsConstructor
@Schema(description = "Asztal")
@Entity
@javax.persistence.Table(name = "rtable")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Table {
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;
    
    @Schema(description = "Asztal elnevezése")
    @NotBlank(message = "error.table.name.notset")
    @NotNull(message = "error.table.name.notset")
    @Size(max=20, message = "error.table.name.long")
    private String name;
    
    @Schema(description = "Aktuálisan használható?l")
    @JsonIgnore
    private boolean available = true;
 
    @Schema(description = "Székek száma az asztalnál")
    @Min(value = 2, message = "error.table.capacity.min")
    private byte capacity;
    
 //   @Schema(description = "Fényképek")
 //   private List<String> imageurls= new ArrayList<>();

    
    @Builder
    public Table(String name, byte capacity) {
        this.name = name;
        this.capacity = capacity;
    }
    
    
}
