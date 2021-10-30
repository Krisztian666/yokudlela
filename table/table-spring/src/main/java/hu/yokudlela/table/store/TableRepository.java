package hu.yokudlela.table.store;

import hu.yokudlela.table.datamodel.Table;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Asztalok kezelése
 * @author (K)risztián
 */
@Repository
public interface TableRepository extends CrudRepository<Table, Long> {
    

    
/**
 * Asztal lekérdezés név alapján
 * @param pName aztal egyedi neve
 * @throws java.lang.NullPointerException Nincs ilyen nevű asztal
 * @return asztal
 */    
     public Table findByName(String pName);
  //  public Table getByName(String pName) throws NullPointerException

/**
 * Asztalok lekérdezése 
 * @param pAvailable false = használaton kívüli
 * @return kiválasztott asztalok listája
 */  
   public List<Table> findByAvailable(boolean pAvailable);  
   // public List<Table> getByAvailable(boolean pAvailable)
   
    
/**
 * Új asztal létesítése az étteremben
 * @param pTable az új Asztal leírása
 * @throws java.lang.Exception  ha már ilyen névvel létezik asztal
 */    
   public Table save(Table pTable); 
 //   public void add(Table pTable) throws Exception
//     public void modify(Table pTable) throws IllegalAccessException, InvocationTargetException{
    
/**
 * Meglévő asztal megszüntetése az étteremben
 * @param pTable az asztal leírása
 * @return sikeres művelet esetén true;
 */    
   public void delete(Table pTable);
//    public boolean delete(Table pTable)
//    public boolean delete(String pName)
    
    
}
