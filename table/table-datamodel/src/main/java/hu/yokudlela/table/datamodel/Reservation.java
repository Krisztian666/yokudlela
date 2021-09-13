package hu.yokudlela.table.datamodel;
import java.time.LocalDateTime;
/**
 * Egy foglalás
 * @author (K)risztián
 */
public class Reservation {
    private String name;
     private LocalDateTime begin;
     private LocalDateTime end;
     private Table table;
     private byte person;
}
