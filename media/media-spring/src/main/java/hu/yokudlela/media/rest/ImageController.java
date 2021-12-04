package hu.yokudlela.media.rest;

import hu.yokudlela.table.service.BusinessException;
import hu.yokudlela.table.utils.logging.AspectLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Krisztian
 */
@RestController()
@RequestMapping(path = "/image")
public class ImageController {
    private final String fileDirPath ="/tmp/up/";
    
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Sikeres képfeltöltés"),
        @ApiResponse(responseCode = "500", description = "Képfeltöltés nem lehetséges")    
    })
    @Operation(summary = "Képfeltöltés",
            security = {
            @SecurityRequirement(name = "apikey",scopes = {"file"}),
            @SecurityRequirement(name = "openid",scopes = {"file"}),
            @SecurityRequirement(name = "oauth2",scopes = {"file"}),
    })       
    @PostMapping(value = "/addFiles", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @AspectLogger   
    public List<ImageProcessModel> addFiles(@RequestPart("files") MultipartFile[] files, ModelMap modelMap) throws IOException {
        modelMap.addAttribute("files", files);
        File f = new File(fileDirPath);
        f.mkdirs();
        List<ImageProcessModel> result = new ArrayList();
        for(MultipartFile f2: files){
            ImageProcessModel imageItem =  ImageProcessModel.builder().id(UUID.randomUUID().toString()).build();
            f = new File(fileDirPath+imageItem.getId());
            f.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(f);
            outputStream.write(f2.getBytes());
            outputStream.close();
            result.add(imageItem);
        }
        return result;
    } 
    
    
    
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Sikeres képletöltés"),
        @ApiResponse(responseCode = "500", description = "Képletöltés nem lehetséges")    
    })
    @Operation(summary = "Képinformációk")       
    @GetMapping(value = "/getimageinfo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ImageProcessModel getImageInfo(@PathVariable("id") String pFileId) throws IOException {
         File f = new File(fileDirPath+pFileId);
        if(f.isFile()){
            return ImageProcessModel.builder().id(pFileId).build();        
        }
        throw new BusinessException();

    } 
    
    
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Sikeres képletöltés"),
        @ApiResponse(responseCode = "500", description = "Képletöltés nem lehetséges")    
    })
    @Operation(summary = "Képletöltés")       
    @GetMapping(value = "/getImage/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable("id") String pFileId) throws IOException {
        File f = new File(fileDirPath+pFileId);
        if(f.isFile()){
            InputStream in = new FileInputStream(f);
            return IOUtils.toByteArray(in);
        }
        throw new BusinessException();
    }

    
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Sikeres képtörlés"),
        @ApiResponse(responseCode = "500", description = "Képtörlés nem sikerült")    
    })
    @Operation(summary = "Képfeltöltés",
            security = {
            @SecurityRequirement(name = "apikey",scopes = {"file"}),
            @SecurityRequirement(name = "openid",scopes = {"file"}),
            @SecurityRequirement(name = "oauth2",scopes = {"file"}),
    })       
    @DeleteMapping(value = "/deleteFile/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @AspectLogger   
    public ImageProcessModel deleteFiles(@PathVariable("id") String pFileId) throws IOException {
        File f = new File(fileDirPath+pFileId);
        if(f.isFile()){
            f.delete();
            return ImageProcessModel.builder().id(pFileId).build();
        }
        throw new BusinessException();
    } 
    
    
    
}
