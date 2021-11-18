package hu.yokudlela.table.service;

import hu.yokudlela.table.datamodel.Reservation;
import hu.yokudlela.table.datamodel.Table;
import hu.yokudlela.table.store.ReservationRepository;
import hu.yokudlela.table.store.TableRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Szabad asztalok kezelése
 * @author (K)risztián
 */
@Service
public class FreeTableService {
    @Autowired
    TableRepository tRep;
    @Autowired
    ReservationRepository rRep;

//    @Cacheable(cacheNames = "table", key = "#root.methodName")
    @AspectLogger
    public List<Table> getAllFree(LocalDateTime pBegin, LocalDateTime pEnd){
        List<Table> allAvailable = tRep.findByAvailable(true);
        List<Reservation> allRes = rRep.findByBeginBetween(pBegin, pEnd);        
        allRes.stream().forEach(e -> allAvailable.remove(e.getTable()));
        return allAvailable;
    }
    
}