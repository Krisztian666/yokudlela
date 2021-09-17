package hu.yokudlela.table.store;

import hu.yokudlela.table.datamodel.Reservation;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Foglalások tárolása
 * @author (K)risztián
 */
public class ReservationRepository {
    private static final List<Reservation> reservations = new ArrayList<>();
    
    
/**
 * Foglalás lekérdezés név kezdeti dátum és személyek száma alapján
 * @param pBegin foglalás kezdete
 * @param pEnd foglalás vége
 * @return asztal
 */    
    public List<Reservation> getBetweenBeginAndEnd(LocalDateTime pBegin, LocalDateTime pEnd){
        return reservations.stream().filter(
                element -> (element.getBegin().isEqual(pBegin) || 
                                element.getBegin().isAfter(pBegin)) && 
                           (element.getEnd().isEqual(pEnd) || element.getEnd().isBefore(pEnd))
                ).collect(Collectors.toList());
    }
    
/**
 * Új foglalás
 * @param pReservation az új Foglalás leírása
 * @throws java.lang.Exception  ha már ilyen névvel és időben létezik foglalás
 */    
    public void add(Reservation pReservation) throws Exception{
        if(getOptionalByNameAndBegin(pReservation.getName(), pReservation.getBegin()).isEmpty()){
            ReservationRepository.reservations.add(pReservation);
        }
        else throw new Exception();
    }
    
    
/**
 * Foglalás törlése
 * @param pName az asztal neve
 * @param pBegin foglalás kezdete
 * @param pPerson személyek száma
 * @return sikeres művelet esetén true;
 */    
    public boolean delete(String pName, LocalDateTime pBegin, byte pPerson){
        Optional<Reservation> tmp = getOptionalByNameAndBegin(pName, pBegin);
        if(tmp.isEmpty()) return false;
        return ReservationRepository.reservations.remove(tmp.get());
    }    
    
/**
 * Foglalás módosítása
 * @param pOldReservation eredeti foglalás adatok
 * @param pNewReservation új foglalás adatok
 * @throws java.lang.IllegalAccessException
 * @throws java.lang.reflect.InvocationTargetException
 */    
    public void modify(Reservation pOldReservation, Reservation pNewReservation) throws IllegalAccessException, InvocationTargetException{
        Optional<Reservation> tmp = getOptionalByNameAndBegin(pOldReservation.getName(), pOldReservation.getBegin());
        if(!tmp.isEmpty()){
            BeanUtils.copyProperties(tmp.get(), pNewReservation);
        }
    }
    
/**
 * Foglalás lekérdezés név kezdeti dátum és személyek száma alapján
 * @param pName foglaló neve
 * @param pBegin foglalás kezdete
 * @return asztal
 */    
    public Optional<Reservation> getOptionalByNameAndBegin(String pName, LocalDateTime pBegin){
        Optional<Reservation> res =reservations.stream().filter(
                element -> element.getName().equals(pName) 
                        && (element.getBegin().isEqual(pBegin) || 
                                (element.getBegin().isBefore(pBegin) && element.getEnd().isAfter(pBegin)))
                ).findFirst();
        return res;
    }
    
/**
 * Foglalás lekérdezés név alapján
 * @param pName foglaló neve
 * @param pBegin foglalás kezdete
 * @throws java.lang.NullPointerException Nincs ilyen névre foglalás
 * @return Foglalás
 */    
    public Reservation getByNameAndBegin(String pName, LocalDateTime pBegin){
        Optional<Reservation> tmp = getOptionalByNameAndBegin(pName, pBegin);
        if(tmp.isEmpty()) throw new NullPointerException();
        return tmp.get();
    }

    
}
