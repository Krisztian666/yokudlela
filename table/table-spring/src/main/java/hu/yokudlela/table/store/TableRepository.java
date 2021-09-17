package hu.yokudlela.table.store;

import hu.yokudlela.table.datamodel.Table;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Asztalok kezelése
 * @author (K)risztián
 */
public class TableRepository {
    private static final List<Table> tables = new ArrayList<>();
    
/**
 * Asztal lekérdezés név alapján
 * @param pName aztal egyedi neve
 * @throws java.lang.NullPointerException Nincs ilyen nevű asztal
 * @return asztal
 */    
    public Table getByName(String pName) throws NullPointerException{
        Optional<Table> tmp = getOptionalByName(pName);
        if(tmp.isEmpty()) throw new NullPointerException();
        return tmp.get();
    }

/**
 * Asztalok lekérdezése 
 * @param pAvailable false = használaton kívüli
 * @return kiválasztott asztalok listája
 */    
    public List<Table> getByAvailable(boolean pAvailable){
       return tables.stream().filter(element -> element.isAvailable()==pAvailable).collect(Collectors.toList());
    }
    
/**
 * Új asztal létesítése az étteremben
 * @param pTable az új Asztal leírása
 * @throws java.lang.Exception  ha már ilyen névvel létezik asztal
 */    
    public void add(Table pTable) throws Exception{
        if(getOptionalByName(pTable.getName()).isEmpty()){
            TableRepository.tables.add(pTable);
        }
        else throw new Exception();
    }
    
/**
 * Meglévő asztal megszüntetése az étteremben
 * @param pTable az asztal leírása
 * @return sikeres művelet esetén true;
 */    
    public boolean delete(Table pTable){
        return TableRepository.tables.remove(pTable);
    }
    
    
/**
 * Meglévő asztal megszüntetése az étteremben
 * @param pName az asztal neve
 * @return sikeres művelet esetén true;
 */    
    public boolean delete(String pName){
        Optional<Table> tmp = getOptionalByName(pName);
        if(tmp.isEmpty()) return false;
        return TableRepository.tables.remove(tmp.get());
    }    
    
/**
 * Út asztal létesítése az étteremben
 * @param pTable az új Asztal leírása
 * @throws java.lang.IllegalAccessException
 * @throws java.lang.reflect.InvocationTargetException
 */    
    public void modify(Table pTable) throws IllegalAccessException, InvocationTargetException{
        Optional<Table> tmp = getOptionalByName(pTable.getName());
        if(!tmp.isEmpty()){
            BeanUtils.copyProperties(tmp.get(), pTable);
        }
    }
    
/**
 * Asztal lekérdezés név alapján
 * @param pName aztal egyedi neve
 * @return asztal
 */    
    public Optional<Table> getOptionalByName(String pName){
        Optional<Table> res =tables.stream().filter(element -> element.getName().equals(pName)).findFirst();
        return res;
    }
    
/**
 * Asztalhasználat engedélyezése
 * @param pName asztal neve
 */    
    public void enableByName(String pName){
        getOptionalByName(pName).get().setAvailable(true);
    }
    
/**
 * Asztalhasználat tiltása
 * @param pName asztal neve
 */    
    public void disableByName(String pName){
        getOptionalByName(pName).get().setAvailable(false);
    }
    
}
