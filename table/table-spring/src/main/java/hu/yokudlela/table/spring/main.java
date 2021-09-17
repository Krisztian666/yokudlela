/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.yokudlela.table.spring;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author oe
 */
public class main {
    public static void main(String[] args) {
        DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse("2021-12-03T10:15:30", LocalDateTime::from);
            
    }
    
}
