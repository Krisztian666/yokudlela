package hu.yokudlela.table.spring;

import hu.yokudlela.table.datamodel.Reservation;
import hu.yokudlela.table.datamodel.Table;
import hu.yokudlela.table.store.ReservationRepository;
import hu.yokudlela.table.store.TableRepository;
import hu.yokudlela.table.store.service.FreeTableService;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author oe
 */
public class TableApplication {

    private static final TableRepository  tableRepo = new TableRepository();
    private static final ReservationRepository reservationRepo = new ReservationRepository();
    private static final FreeTableService freeTableService = new FreeTableService();
   
    
    public static void main(String[] args) throws Exception {
        
        tableRepo.add(Table.builder()
                .name("A1")
                .capacity((byte)2)
                .build());
        tableRepo.add(Table.builder()
                .name("A2")
                .capacity((byte)4)
                .build());
        
        reservationRepo.add(Reservation.builder()
                .name("Borcsa születésnap")
                .person((byte)2)
                .table(tableRepo.getByName("A1"))
                .begin(LocalDateTime.now().minusHours(1))
                .end(LocalDateTime.now().plusHours(1))
                .build());
        
        List<Table> free= freeTableService.getAllFree(LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
        free.stream().forEach(item ->{System.out.println(item.getName());});
    }
    
}
