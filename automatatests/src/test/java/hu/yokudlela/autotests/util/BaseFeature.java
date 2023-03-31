package hu.yokudlela.autotests.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.CookieManager;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author krisztian
 */

public class BaseFeature {
    protected static String correlationId = UUID.randomUUID().toString();
    protected static HashMap<String, ServiceConfig> serviceConfigs = new HashMap<>();
    protected static final String TEST_FILES_PATH = "src/test/resources/test_objects/";
    protected static final String TEST_USER = "test";

    // * network
    protected static HashMap<String,Object> response = new HashMap<>();
    protected Map<String, Object> token;

    protected Map<String, String> headers = new HashMap<>();

    protected String urlPrefix = "http://localhost:3001";


    // * log
    protected static boolean stepIsNetworkCall;
    protected static String logRequest="", logResponse="";
    protected static Integer logStatusCode;
    protected static ServiceConfig url;
  
    protected ServiceConfig getServiceConfig(String pUrlPostFix){
    String url= null;
        String key ;
        if(pUrlPostFix.startsWith("/")){        
            Iterator it = serviceConfigs.keySet().iterator();
            while (it.hasNext()) {
                key = (String)it.next();
                if(pUrlPostFix.startsWith(key)){
                    return serviceConfigs.get(key);
                }
                
            }
            
        }
        return new ServiceConfig("krisztain@mozilla.hu", pUrlPostFix, "api", "key", "http", "channel", "correlation");
    }

    protected HashMap<String, Object> parseJsonStringToMap(String pResponse) throws JsonProcessingException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        pResponse = resolveResponseParams(pResponse).trim();

        HashMap<String, Object> tmp =null;
        if (pResponse.startsWith("{")) {
            tmp = new ObjectMapper().readValue(pResponse, HashMap.class);
            Iterator it = tmp.keySet().iterator();
            String tmpKey;
            Object tmpValue;
            while (it.hasNext()) {
                tmpKey = (String) it.next();
                tmpValue = tmp.get(tmpKey);
                if (tmpValue instanceof String
                        && ((String) tmpValue).startsWith("{")
                        && ((String) tmpValue).endsWith("}")) {
                    tmp.put(tmpKey, parseJsonStringToMap((String) tmpValue));
                }
            }
        }
        else if(pResponse.startsWith("[")){
            List list = new ObjectMapper().readValue(pResponse, List.class);
            tmp = new HashMap<>();
            tmp.put("array", list);
        }
        return tmp;
    }

    protected String parseJsonFileToString(String fileNameWithPath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(String.format("%s%s", TEST_FILES_PATH, fileNameWithPath))));
    }

    protected Map<String,Object> parseJWT(String jwtToken) {
            DecodedJWT jwt = JWT.decode(jwtToken);
            Map<String,Claim> tmp=jwt.getClaims();
            Map<String,Object> tmp0=new HashMap<>();
            Iterator it = tmp.keySet().iterator();
            Object t;
            while(it.hasNext()){
                t=it.next();
                try{tmp0.put((String) t, tmp.get(t).asMap());}
                catch(JWTDecodeException e){
                    try{tmp0.put((String) t, tmp.get(t).asList(Object.class));}
                    catch(JWTDecodeException e0){
                        try{tmp0.put((String) t, tmp.get(t).asString());}
                        catch(JWTDecodeException e1){
                            try{tmp0.put((String) t, tmp.get(t).asLong());}
                            catch(JWTDecodeException e2){
                                try{tmp0.put((String) t, tmp.get(t).asBoolean());}
                                catch(JWTDecodeException e3){
                                    try{tmp0.put((String) t, tmp.get(t).asDate());}
                                catch(JWTDecodeException e4){
                                }}}}}}}
            return tmp0;     
    }            
 
    
    
    
    public String parse(String docString) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String res = resolveResponseParams(docString);
        res = resolveJwtParam(res);
        res = resolveRandomParams(res);        
        res = resolveUUDIParams(res);
        return resolveCharacter(res);
    }

    private String resolveUUDIParams(String docString) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {        
        return docString.replaceAll("\\$\\{uuid}", UUID.randomUUID().toString());        
    }
    
    private String resolveRandomParams(String docString) {
        String doc;
        while(docString.contains("${random(")){
            int idx0 = docString.indexOf("${random(");
            int idx1 = docString.indexOf(",",idx0);
            int idx2 = docString.indexOf(")",idx1+1);
            
            int idx3 = docString.indexOf("}",idx1);
            long min = Long.parseLong(docString.substring(idx0+"${random(".length(),idx1));
            long max = Long.parseLong(docString.substring(idx1+1,idx2));
            Double d = Math.ceil(Math.random()*(max-min))+min;
            doc = docString.substring(0, idx0)+d.longValue()+docString.substring(idx3+1);
            docString = doc;
        }
        return docString;
    }    

    private String resolveCharacter(String docString) {
        String doc;
        while(docString.contains("${char(")){
            int idx0 = docString.indexOf("${char(");
            int idx1 = docString.indexOf(",",idx0);
            int idx2 = docString.indexOf(")",idx1+1);
            
            int idx3 = docString.indexOf("}",idx1);
            String scr = docString.substring(idx0+"${char(".length(),idx1);
            int charIndex = Integer.parseInt(docString.substring(idx1+1,idx2));
            doc = docString.substring(0, idx0)+scr.substring(charIndex,charIndex+1)+docString.substring(idx3+1);
            docString = doc;
        }
        return docString;
    }    

    
    private String resolveResponseParams(String docString) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String doc;
        while(docString.contains("${response(")){
            int idx0 = docString.indexOf("${response(");
            int idx1 = docString.indexOf(")",idx0);
            int idx2 = docString.indexOf("}",idx1);
            String responseKey = docString.substring(idx0+"${response(".length(),idx1);
            String value = BeanUtils.getProperty(response.get(responseKey), docString.substring(idx1+2, idx2));
            doc = docString.substring(0,idx0).concat(value).concat(docString.substring(idx2+1));
            docString = doc;
        }
        return docString;
    }
    
        private String resolveJwtParam(String docString) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String doc;
        while(docString.contains("${jwtcookie(")){
            int idx0 = docString.indexOf("${jwtcookie(");
            int idx1 = docString.indexOf(")",idx0);
            int idx2 = docString.indexOf("}",idx1);

            String value = BeanUtils.getProperty(token, docString.substring(idx1+2, idx2));
            doc = docString.substring(0,idx0).concat(value).concat(docString.substring(idx2+1));
            docString = doc;
        }
        System.out.println("TMP0:"+docString);
        return docString;
    }


   
}



