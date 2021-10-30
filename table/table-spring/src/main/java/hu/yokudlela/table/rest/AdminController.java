package hu.yokudlela.table.rest;

import hu.yokudlela.table.datamodel.Table;
import hu.yokudlela.table.spring.ApiError;
import hu.yokudlela.table.store.TableRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.List;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessToken.Access;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author (K)risztián
 */
@RestController()
@RequestMapping(path = "/admin")
@Validated
public class AdminController {

    @Autowired
    TableRepository tableService;

    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "A keresett asztal", 
	    content = { @Content(mediaType = "application/json", 
	    schema = @Schema(implementation = Table.class)) }),
	@ApiResponse(responseCode = "400", description = "Nem megfelelő kérés paraméterek (validációs hiva)", 
	    content = { @Content(mediaType = "application/json", 
	    schema = @Schema(implementation = ApiError.class)) }),
	@ApiResponse(responseCode = "500", description = "Sikertelen lekérdezés", 
	    content = { @Content(mediaType = "application/json", 
	    schema = @Schema(implementation = ApiError.class)) })            
    })
    @Operation(summary = "Asztal lekérdezés név alapján")
    @GetMapping(path = "/getbyname/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Table getByName(
        @Parameter(description="Asztal neve", required = false, example = "A1")
        @PathVariable(name = "name", required = false)
        @NotEmpty(message = "error.table.name.notset")
        @NotBlank(message = "error.table.name.notset")
        @Size(min=2, max = 10, message = "error.table.name.short_or_long")
        @Pattern(regexp = "^[A-Z][a-zA-Z0-9]+$", message = "error.table.name.pattern_is_bad")
        String pId) throws Exception {
        return tableService.getByName(pId);
    }
    

    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Sikeres lekérdezés", 
	    content = { @Content(mediaType = "application/json",  
	    array = @ArraySchema(schema = @Schema(implementation = Table.class))) }),
      	@ApiResponse(responseCode = "400", description = "Nem megfelelő paraméter", 
	    content = { @Content(mediaType = "application/json", 
	    schema = @Schema(implementation = ApiError.class)) }),
	@ApiResponse(responseCode = "500", description = "Sikertelen lekérdezés", 
	    content = { @Content(mediaType = "application/json", 
	    schema = @Schema(implementation = ApiError.class)) })            
    })
    @Operation(summary = "Asztalok lekérdezés státusz alapján")
    @GetMapping(path = "/getbystate/{state}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Table> getByState(
        @Parameter(description = "Asztal státusz", example = "true")
        @Pattern(regexp = "^(true|false)")
        @PathVariable(name = "state") String pActive) throws Exception {
        return tableService.getByAvailable(Boolean.valueOf(pActive));
    }

    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Sikeres felvitel", 
	    content = { @Content(mediaType = "application/json", 
	    schema = @Schema(implementation = Table.class)) }),
	@ApiResponse(responseCode = "403", description = "Nincs megfelelő jogosultságod",
	    content = { @Content(mediaType = "application/json") }),
      	@ApiResponse(responseCode = "401", description = "Lejárt token",
	    content = { @Content(mediaType = "application/json") }),
      	@ApiResponse(responseCode = "302", description = "Nincs bejelentkezve, átirányítás a login oldalra",
	    content = { @Content(mediaType = "application/json") }),
      	@ApiResponse(responseCode = "400", description = "Nem megfelelő kérés paraméterek (validációs hiva)", 
	    content = { @Content(mediaType = "application/json", 
	    schema = @Schema(implementation = ApiError.class)) }),
    	@ApiResponse(responseCode = "500", description = "Asztal már létezik",
	    content = { @Content(mediaType = "application/json") })
    })
    @Operation(summary = "Új aztal felvitele",
            security = {
            @SecurityRequirement(name = "apikey",scopes = {"table"}),
            @SecurityRequirement(name = "openid",scopes = {"table"}),
            @SecurityRequirement(name = "oauth2",scopes = {"table"}),
    })
    @PostMapping(path = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
   public Table add(
            Principal principal,
           @Valid
            @Parameter(description = "Az új asztal",required = true) @RequestBody(required = true) Table pData) throws Exception {
        KeycloakPrincipal kPrincipal = (KeycloakPrincipal) principal;
        AccessToken token = kPrincipal.getKeycloakSecurityContext().getToken();
        Access customClaims = token.getResourceAccess("account");
        System.out.println("ROLES:"+customClaims.getRoles());
        tableService.add(pData);
        return pData;
    }

        
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Sikeres művelet", 
	    content = { @Content(mediaType = "application/json") }),
	@ApiResponse(responseCode = "403", description = "Nincs megfelelő jogosultságod",
	    content = { @Content(mediaType = "application/json") }),
      	@ApiResponse(responseCode = "401", description = "Lejárt token",
	    content = { @Content(mediaType = "application/json") }),
      	@ApiResponse(responseCode = "302", description = "Nincs bejelentkezve, átirányítás a login oldalra",
	    content = { @Content(mediaType = "application/json") }),
      	@ApiResponse(responseCode = "400", description = "Nem megfelelő kérés paraméterek (validációs hiva)", 
	    content = { @Content(mediaType = "application/json", 
	    schema = @Schema(implementation = ApiError.class)) }),
    	@ApiResponse(responseCode = "500", description = "Asztal nem létezik",
	    content = { @Content(mediaType = "application/json") })
    })
    @Operation(summary = "Asztal törlése")
    @DeleteMapping(path = "/delete/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(
        @Parameter(description = "Asztal neve", required = true, example = "A1") 
        @PathVariable(name = "name", required = true) 
        @NotEmpty(message = "error.table.name.notset")
        @NotBlank(message = "error.table.name.notset")
        @Size(min=2, max = 10, message = "error.table.name.short_or_long")
        @Pattern(regexp = "^[A-Z][a-zA-Z0-9]+$", message = "error.table.name.pattern_is_bad")        
                String pId) {
        tableService.delete(pId);
    }

    public void deleteImageUrl(@PathVariable(name = "name") String pId) {
    }

    public void addImageUrl(@PathVariable(name = "name") String pId) {
    }
    
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Sikeres művelet", 
	    content = { @Content(mediaType = "application/json", 
	    schema = @Schema(implementation = Table.class)) }),
	@ApiResponse(responseCode = "403", description = "Nincs megfelelő jogosultságod",
	    content = { @Content(mediaType = "application/json") }),
      	@ApiResponse(responseCode = "401", description = "Lejárt token",
	    content = { @Content(mediaType = "application/json") }),
      	@ApiResponse(responseCode = "302", description = "Nincs bejelentkezve, átirányítás a login oldalra",
	    content = { @Content(mediaType = "application/json") }),
      	@ApiResponse(responseCode = "400", description = "Nem megfelelő kérés paraméterek (validációs hiva)", 
	    content = { @Content(mediaType = "application/json", 
	    schema = @Schema(implementation = ApiError.class)) }),
    	@ApiResponse(responseCode = "500", description = "Asztal nem létezik",
	    content = { @Content(mediaType = "application/json") })
    })
    @Operation(
                        security = {
            @SecurityRequirement(name = "apikey",scopes = {"table"}),
            @SecurityRequirement(name = "openid",scopes = {"table"}),
            @SecurityRequirement(name = "oauth2",scopes = {"table"}),
    },

            summary = "Asztal engedélyezése")
    @PutMapping(path = "/enable/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void enable(
        @Parameter(description = "Asztal neve", required = true, example = "A1")
                @NotEmpty(message = "error.table.name.notset")
        @NotBlank(message = "error.table.name.notset")
        @Size(min=2, max = 10, message = "error.table.name.short_or_long")
        @Pattern(regexp = "^[A-Z][a-zA-Z0-9]+$", message = "error.table.name.pattern_is_bad")
        @PathVariable(name = "name", required = true) String pId) {
        tableService.enableByName(pId);
    }

    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Sikeres művelet", 
	    content = { @Content(mediaType = "application/json", 
	    schema = @Schema(implementation = Table.class)) }),
	@ApiResponse(responseCode = "403", description = "Nincs megfelelő jogosultságod",
	    content = { @Content(mediaType = "application/json") }),
      	@ApiResponse(responseCode = "401", description = "Lejárt token",
	    content = { @Content(mediaType = "application/json") }),
      	@ApiResponse(responseCode = "302", description = "Nincs bejelentkezve, átirányítás a login oldalra",
	    content = { @Content(mediaType = "application/json") }),
      	@ApiResponse(responseCode = "400", description = "Nem megfelelő kérés paraméterek (validációs hiva)", 
	    content = { @Content(mediaType = "application/json", 
	    schema = @Schema(implementation = ApiError.class)) }),
    	@ApiResponse(responseCode = "500", description = "Asztal már létezik",
	    content = { @Content(mediaType = "application/json") })
    })
    @Operation(
            security = {
            @SecurityRequirement(name = "apikey",scopes = {"table"}),
            @SecurityRequirement(name = "openid",scopes = {"table"}),
            @SecurityRequirement(name = "oauth2",scopes = {"table"}),
    },

        summary = "Asztal módosítása")
    @PutMapping(path = "/disable/{name}", produces = MediaType.APPLICATION_JSON_VALUE)    
    public void disable(
        @Parameter(description = "Asztal neve", required = true)
                @NotEmpty(message = "error.table.name.notset")
        @NotBlank(message = "error.table.name.notset")
        @Size(min=2, max = 10, message = "error.table.name.short_or_long")
        @Pattern(regexp = "^[A-Z][a-zA-Z0-9]+$", message = "error.table.name.pattern_is_bad")
        @PathVariable(name = "name", required = true) String pId) {
        tableService.disableByName(pId);
    }

    
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Sikeres módosítás", 
	    content = { @Content(mediaType = "application/json", 
	    schema = @Schema(implementation = Table.class)) }),
	@ApiResponse(responseCode = "403", description = "Nincs megfelelő jogosultságod",
	    content = { @Content(mediaType = "application/json") }),
      	@ApiResponse(responseCode = "401", description = "Lejárt token",
	    content = { @Content(mediaType = "application/json") }),
      	@ApiResponse(responseCode = "302", description = "Nincs bejelentkezve, átirányítás a login oldalra",
	    content = { @Content(mediaType = "application/json") }),
      	@ApiResponse(responseCode = "400", description = "Nem megfelelő kérés paraméterek (validációs hiva)", 
	    content = { @Content(mediaType = "application/json", 
	    schema = @Schema(implementation = ApiError.class)) }),
    	@ApiResponse(responseCode = "500", description = "Asztal nem létezik",
	    content = { @Content(mediaType = "application/json") })
    })
    @Operation(
                        security = {
            @SecurityRequirement(name = "apikey",scopes = {"table"}),
            @SecurityRequirement(name = "openid",scopes = {"table"}),
            @SecurityRequirement(name = "oauth2",scopes = {"table"}),
    },

            summary = "Asztal módosítása")
    @PutMapping(path = "/modify", produces = MediaType.APPLICATION_JSON_VALUE)
    public Table modify(
            @Valid
        @Parameter(description = "Asztal", required = true) 
        @RequestBody(required = true) Table pTable) throws IllegalAccessException, InvocationTargetException {
        tableService.modify(pTable);
        return pTable;
    }
    
}
