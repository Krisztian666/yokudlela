package hu.yokudlela.table.service;

import hu.yokudlela.table.datamodel.Reservation;
import hu.yokudlela.table.datamodel.Table;
import hu.yokudlela.table.store.ReservationRepository;
import hu.yokudlela.table.store.TableRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Szabad asztalok kezelése
 * @author (K)risztián
 */
@Service
public class FreeTableService {
    TableRepository tRep = new TableRepository();
    ReservationRepository rRep = new ReservationRepository();
        
    public List<Table> getAllFree(LocalDateTime pBegin, LocalDateTime pEnd){
        List<Table> allAvailable = tRep.getByAvailable(true);
        List<Reservation> allRes = rRep.getBetweenBeginAndEnd(LocalDateTime.MIN, LocalDateTime.MAX);
        
        allRes.stream().forEach(e -> allAvailable.remove(e.getTable()));
        return allAvailable;
    }
    
}