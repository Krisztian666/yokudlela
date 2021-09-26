package hu.yokudlela.table.datamodel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotBlank(message = "error.reservation.name.notset")
    @NotNull(message = "error.reservation.name.notset")
    @Size(max=20,min = 3, message = "error.reservation.name.long")
    private String name;

    @Schema(description = "Foglalás kezdete", example = "2021-10-10T10:00:10")
    @NotBlank(message = "error.reservation.name.notset")
    @NotNull(message = "error.reservation.name.notset")
    @JsonSerialize(using = ContactSerializer.class)
    private String contact;

    @Schema(description = "Foglalás kezdete", example = "2021-10-10T10:00:10")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)    
    private LocalDateTime begin;
    
    @Schema(description = "Foglalás vége", example = "2021-10-10T10:00:10")
    @Future(message = "error.reservation.begin.past")
    private LocalDateTime end;
    
    @Schema(description = "Lefoglalt asztal")
    private Table table;
    
    @Schema(description = "Személyek száma")
    @Min(value=1, message = "error.reservation.person.few")    
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
