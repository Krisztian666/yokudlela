package hu.yokudlela.table.service.cliens;

import hu.yokudlela.media.java.clients.invoker.ApiClient;
import hu.yokudlela.media.java.clients.invoker.ApiException;
import hu.yokudlela.media.java.clients.invoker.JSON;
import hu.yokudlela.media.java.clients.invoker.Pair;
import hu.yokudlela.media.java.clients.invoker.RFC3339DateFormat;
import hu.yokudlela.media.java.clients.invoker.auth.ApiKeyAuth;
import hu.yokudlela.media.java.clients.invoker.auth.Authentication;
import hu.yokudlela.media.java.clients.invoker.auth.OAuth;
import hu.yokudlela.media.java.clients.invoker.auth.OAuthFlow;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.MultiPart;

/**
 *
 * @author oe
 */
public class CustomApiClient extends ApiClient{

    public CustomApiClient() {super();}
    
    protected void updateParamsForAuth(String[] authNames, List<Pair> queryParams, Map<String, String> headerParams) {
    for (String authName : authNames) {
      Authentication auth = authentications.get(authName);
      if (auth != null) 
      auth.applyToParams(queryParams, headerParams);
    }
  }
    
}
