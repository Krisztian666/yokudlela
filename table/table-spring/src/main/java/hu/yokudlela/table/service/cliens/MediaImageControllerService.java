
package hu.yokudlela.table.service.cliens;

import hu.yokudlela.media.java.clients.api.ImageControllerApi;
import hu.yokudlela.media.java.clients.model.ImageProcessModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
/**
 * @author Krisztian
 */
@Service
@Slf4j
public class MediaImageControllerService {
    
    @Value("${client.media}")
    private String path;
    
    public ImageControllerApi getClientInstance(){
        ImageControllerApi api = new ImageControllerApi();
        api.getApiClient().setBasePath(path);
        return new ImageControllerApi();
    }
    
    public ImageControllerApi getClientInstanceWithToken(String pToken){
        ImageControllerApi api = new ImageControllerApi();
        api.getApiClient().setBasePath(path);
        api.getApiClient().setAccessToken (pToken);
        return new ImageControllerApi();
    }
    
    
    public List<ImageProcessModel> uploadTempFileWithToken(String pToken, File pTmpFile) throws IOException{
        log.info(path);
        final Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
        
        final FileDataBodyPart filePart = new FileDataBodyPart("files", pTmpFile);
        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
        final FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.bodyPart(filePart);
      
        final WebTarget target = client.target(path+"/image/addFiles");
        
        final Response response = target.request().header("Authorization", pToken).post(Entity.entity(multipart, multipart.getMediaType()));
    
        formDataMultiPart.close();
        multipart.close();
       

        return (List<ImageProcessModel>)response.readEntity(ArrayList.class);
    
    }
}
