package hu.yokudlela.table.datamodel;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class ContactSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String tmpString, 
                          JsonGenerator jsonGenerator, 
                          SerializerProvider serializerProvider) 
                          throws IOException, JsonProcessingException {
        if(tmpString.contains("@"))
            jsonGenerator.writeObject("mailto:".concat(tmpString));
        else if(tmpString.startsWith("+"))
            jsonGenerator.writeObject("tel:".concat(tmpString));
        else
            jsonGenerator.writeNull();            
    }   
}
