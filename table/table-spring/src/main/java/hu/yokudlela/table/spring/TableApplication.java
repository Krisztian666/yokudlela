package hu.yokudlela.table.spring;

import hu.yokudlela.table.datamodel.Table;

/**
 * @author oe
 */
public class TableApplication {

    public static void main(String[] args) {
        Table tmp = Table.builder()
                .name("A1")
                .capacity((byte)2)
                .build();
        System.out.println(tmp.getName());
    }
    
}
