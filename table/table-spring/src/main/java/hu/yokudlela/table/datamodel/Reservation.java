package hu.yokudlela.table.datamodel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
/**
 * Egy foglalás
 * @author (K)risztián
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode()
@Schema(description = "Foglalás")
public class Reservation {
    @Schema(description = "Foglaló neve")
    private String name;

    @Schema(description = "Foglalás kezdete", example = "2021-10-10T10:00:10")
    @JsonSerialize(using = ContactSerializer.class)
    private String contact;

    @Schema(description = "Foglalás kezdete", example = "2021-10-10T10:00:10")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)    
    private LocalDateTime begin;
    
    @Schema(description = "Foglalás vége", example = "2021-10-10T10:00:10")
    private LocalDateTime end;
    
    @Schema(description = "Lefoglalt asztal")
    private Table table;
    
    @Schema(description = "Személyek száma")
    private byte person;

    @Builder
    public Reservation(String name, LocalDateTime begin, LocalDateTime end, Table table, byte person) {
        this.name = name;
        this.begin = begin;
        this.end = end;
        this.table = table;
        this.person = person;
    }
     
     
}
