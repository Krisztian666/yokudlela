package hu.yokudlela.table.store;

import hu.yokudlela.table.datamodel.Reservation;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * Foglalások tárolása
 * @author (K)risztián
 */
@Repository
public interface ReservationRepository extends CrudRepository<Reservation, String> {
    
    
    
/**
 * Foglalás lekérdezés név kezdeti dátum és személyek száma alapján
 * @param pBegin foglalás kezdete
 * @param pEnd foglalás vége
 * @return asztal
 */    
       
    
    
    public List<Reservation> findByBeginBetween(LocalDateTime pBegin, LocalDateTime pEnd);       
 //   public List<Reservation> getBetweenBeginAndEnd(LocalDateTime pBegin, LocalDateTime pEnd)
   
    
    /**
 * Új foglalás
 * @param pReservation az új Foglalás leírása
 * @return 
 */    
    
    public Reservation save(Reservation pReservation);
//    public void add(Reservation pReservation) throws Exception
//    public void modify(Reservation pOldReservation, Reservation pNewReservation) throws IllegalAccessException, InvocationTargetException{
 
/**
 * Foglalás törlése
 * @param peresvation
 */    

    public void delete(Reservation peresvation);
//    public boolean delete(String pName, LocalDateTime pBegin, byte pPerson)
    
    
    
    
/**
 * Foglalás lekérdezés név alapján
 * @param pName foglaló neve
 * @param pBegin foglalás kezdete
 * @throws java.lang.NullPointerException Nincs ilyen névre foglalás
 * @return Foglalás
 */    
    public Reservation findByNameAndBegin(String pName, LocalDateTime pBegin);
//    public Optional<Reservation> getOptionalByNameAndBegin(String pName, LocalDateTime pBegin)
//    public Reservation getByNameAndBegin(String pName, LocalDateTime pBegin)

    
}
