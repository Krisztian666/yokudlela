package hu.yokudlela.autotests.util; /**
 * @author krisztian
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
public class AMQPUtils {
    
    private static final String QUEUE_NAME = "default-queue";
    private static final String HOST_NAME = "localhost";
    private static final String USER_NAME = "guest";
    private static final String PASSWORD = "guest";
    private static final boolean DURABLE = true;
    private static final String ROUTING_KEY = "";

    /**
     * Kapcsolódik egy Rabbit Queue-hoz és elküld egy üzenetet.
     * @param pHost Host
     * @param pUser Hitelesítéshez szükséges felhasználó név  
     * @param pPassword Hitelesítendő felhasználó jelszava
     * @param pQueueName Queue neve
     * @param pCorrelationId Corr. Id
     * @param message Adat
     * @throws Exception Bármilyen küldési vagy serializációs hiba.
     */
    public static void connectSendMessage (
            String pHost, String pUser, String pPassword,
            String pQueueName, String pCorrelationId, Object message) throws Exception {
        AMQP.BasicProperties properties = new AMQP.BasicProperties(null,null,null,null, null, pCorrelationId, null, null, null, null, null, null, null, null);


        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(resolveUserName(pUser));
        factory.setPassword(resolvePassword(pPassword));        
        factory.setHost(resolveHostName(pHost));

        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {

            // * String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
            channel.queueDeclare(resolveQueue(pQueueName), DURABLE, false, false, null);
            channel.basicPublish(ROUTING_KEY, resolveQueue(pQueueName), properties, ow.writeValueAsBytes(message));
            log.info("Rabbit message sent with message " + message);
        }
    }

    public static byte[] connectReceiveMessage (
            String pHost, String pUser, String pPassword, String pQueueName) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(resolveHostName(pHost));
        factory.setUsername(resolveUserName(pUser));
        factory.setPassword(resolvePassword(pPassword));

        try (Connection connection = factory.newConnection();
        Channel channel = connection.createChannel()) {
            channel.queueDeclare(resolveQueue(pQueueName), DURABLE, false, false, null);
            GetResponse getResponse = channel.basicGet(resolveQueue(pQueueName), true);
            log.info("Rabbit message received.");
            return getResponse.getBody();
        }
    }
    // * https://stackabuse.com/definitive-guide-to-jackson-objectmapper-serialize-and-deserialize-java-objects/

    public static void purgeMessages(String pHost, String pUser, String pPassword, String pQueueName) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(resolveHostName(pHost));
        factory.setUsername(resolveUserName(pUser));
        factory.setPassword(resolvePassword(pPassword));

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queuePurge(resolveQueue(pQueueName));
            log.info("Rabbit messages purged.");
        }
    }


    private static String resolveHostName(String pHostName){
        return (pHostName==null)?HOST_NAME:pHostName;
    }
        
    private static String resolveUserName(String pUserName){
        return (pUserName==null)?USER_NAME:pUserName;
    }
    
    private static String resolvePassword(String pPassword){
        return (pPassword==null)?PASSWORD:pPassword;
    }
    
    private static String resolveQueue(String pQueueName){
        return (pQueueName==null)?QUEUE_NAME:pQueueName;
    }

    private AMQPUtils(){}

}
