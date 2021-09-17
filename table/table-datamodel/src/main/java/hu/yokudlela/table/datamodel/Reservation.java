package hu.yokudlela.table.datamodel;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * Egy foglalás
 * @author (K)risztián
 */
@Data
@EqualsAndHashCode()
public class Reservation {
    private String name;
     private LocalDateTime begin;
     private LocalDateTime end;
     private Table table;
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
