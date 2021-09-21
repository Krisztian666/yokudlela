package hu.yokudlela.table.rest;

import hu.yokudlela.table.datamodel.Reservation;
import hu.yokudlela.table.datamodel.Table;
import hu.yokudlela.table.store.ReservationRepository;
import hu.yokudlela.table.service.FreeTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Foglalásokat kezelő REST kontroller
 * @author (K)risztián
 */
@RestController()
@RequestMapping(path = "/reservation")
public class ReservationController {

    @Autowired
    private ReservationRepository service;
    
    @Autowired
    private FreeTableService  free;

    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Sikeres lekérdezés", 
	    content = { @Content(mediaType = "application/json",  
	    array = @ArraySchema(schema = @Schema(implementation = Reservation.class))) }),
    })
    @Operation(summary = "Foglalások lekérdezése")    
    @GetMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)    
    public List<Reservation> getReservations(
                @Parameter(description = "Lekérdezés kezdete", required = true) @RequestParam(name = "start", required = true) String pBegin, 
                @Parameter(description = "Lekérdezés vége", required = true) @RequestParam(name = "stop", required = true) String pEnd){
        return service.getBetweenBeginAndEnd(
            DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(pBegin, LocalDateTime::from),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(pEnd, LocalDateTime::from));
    }
    
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Sikeres lekérdezés", 
	    content = { @Content(mediaType = "application/json",  
	    array = @ArraySchema(schema = @Schema(implementation = Table.class))) }),
    })
    @Operation(summary = "Szabad asztalok lekérdezése")    
    @GetMapping(path = "/free", produces = MediaType.APPLICATION_JSON_VALUE)    
    public List<Table> getFree(
                @Parameter(description = "Lekérdezés kezdete", required = true,  example = "2021-12-03T10:15:30" ) @RequestParam(name = "start", required = true) String pBegin, 
                @Parameter(description = "Lekérdezés vége", required = true, example = "2021-12-03T10:15:30") @RequestParam(name = "stop", required = true) String pEnd){
   
        return free.getAllFree(
            DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(pBegin, LocalDateTime::from),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(pEnd, LocalDateTime::from));
    }

    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Sikeres Foglalás"),
        @ApiResponse(responseCode = "500", description = "Foglalás nem lehetséges")    
    })
    @Operation(summary = "Asztal foglalás")    
    @PostMapping(path = "/save")
    public void save(@Parameter(description = "Foglalás", required = true) @RequestBody(required = true) Reservation pData) throws Exception{
         service.add(pData);
    }
}
