package hu.yokudlela.media.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.yokudlela.table.utils.logging.AspectLogger;
import hu.yokudlela.table.utils.logging.CustomRequestLoggingFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;
import static net.logstash.logback.argument.StructuredArguments.keyValue;
import org.apache.commons.codec.binary.Base64;
import org.jboss.logging.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Service;

/**
 * @author (K)risztián
 */
@Slf4j
@Service
public class Receiver {//implements MessageListener {

    private final String fileDirPath ="/tmp/up/";
 
    private CountDownLatch latch = new CountDownLatch(1);
    

    @AspectLogger
    @RabbitListener(queues = "media.file")
    public void onMessage(Message message) throws IOException {
        //user = new RequestBean();
//        MDC.put("clientid",message.getMessageProperties().getHeaders().get("client").toString());
        MDC.put(CustomRequestLoggingFilter.USER_ID,message.getMessageProperties().getHeaders().get("user").toString());
        MDC.put(CustomRequestLoggingFilter.REQUEST_ID,message.getMessageProperties().getCorrelationId());   
        
        QueueModel c = null;
        c = (new ObjectMapper()).readValue(message.getBody(), QueueModel.class);                
        File f = new File(fileDirPath);
        f.mkdirs();
        f = new File(fileDirPath+UUID.randomUUID().toString());
        f.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(f);
        outputStream.write(Base64.decodeBase64(c.getBase64data()));
        outputStream.close();
                
    }
    
}
