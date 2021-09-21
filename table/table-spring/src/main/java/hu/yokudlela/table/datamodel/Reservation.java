package hu.yokudlela.table.datamodel;
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
@EqualsAndHashCode()
@NoArgsConstructor
public class Reservation {
     private String name;
     private String contact;
     private LocalDateTime begin;
     private LocalDateTime end;
     private Table table;
     private byte person;

     @Builder
     public Reservation(String name, String contact, LocalDateTime begin, LocalDateTime end, Table table, byte person) {
          this.name = name;
          this.contact = contact;
          this.begin = begin;
          this.end = end;
          this.table = table;
          this.person = person;
     }
}
