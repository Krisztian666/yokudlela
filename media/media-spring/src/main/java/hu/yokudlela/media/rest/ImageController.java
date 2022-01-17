package hu.yokudlela.media.rest;

import hu.yokudlela.media.rabbit.QueueModel;
import hu.yokudlela.table.service.BusinessException;
import hu.yokudlela.table.utils.logging.AspectLogger;
import hu.yokudlela.table.utils.logging.CustomRequestLoggingFilter;
import hu.yokudlela.table.utils.request.RequestBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.Null;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.Part;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
    
    @Autowired
    private RequestBean request;
    
   @Autowired
    private RabbitTemplate template;


    @Autowired
    private TopicExchange exchange;
    
    @Value("${app.mq.topicname:media-fileupload}")
    private String mqTopic;
    
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
    public List<ImageProcessModel> addFiles(
        @Parameter(description = "Hívó kérés azonosítója", required = false) @RequestHeader(value=CustomRequestLoggingFilter.REQUEST_ID, required = false, defaultValue = "") String clientid,
        @RequestPart("file") MultipartFile file
        ) throws IOException {
        File f = new File(fileDirPath);
        f.mkdirs();
        List<ImageProcessModel> result = new ArrayList();
            ImageProcessModel imageItem =  ImageProcessModel.builder().id(UUID.randomUUID().toString()).build();
            f = new File(fileDirPath+imageItem.getId());
            f.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(f);
            outputStream.write(file.getBytes());
            outputStream.close();
            result.add(imageItem);
        return result;
    } 
    
    
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
    @PostMapping(value = "/addAsyncFiles", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @AspectLogger   
    public void addAsyncFiles(
        @Parameter(description = "Hívó kérés azonosítója", required = false) @RequestHeader(value=CustomRequestLoggingFilter.REQUEST_ID, required = false, defaultValue = "") String clientid,
        @RequestPart("file") MultipartFile file) throws IOException {
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        template.convertAndSend(exchange.getName(),"foo.bar.#",
                QueueModel.builder().base64data(Base64.encodeBase64String(file.getBytes())).build(),
                m -> {
                    m.getMessageProperties().getHeaders().put("client", request.getClient());
                    m.getMessageProperties().getHeaders().put("user", request.getUser());     
                    return m;
                },new CorrelationData(request.getRequestId()));
        
    } 
    
    
    
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Sikeres képletöltés"),
        @ApiResponse(responseCode = "500", description = "Képletöltés nem lehetséges")    
    })
    @Operation(summary = "Képinformációk")       
    @GetMapping(value = "/getimageinfo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ImageProcessModel getImageInfo(
        @PathVariable("id") String pFileId,
        @Parameter(description = "Hívó kérés azonosítója", required = false) @RequestHeader(value=CustomRequestLoggingFilter.REQUEST_ID, required = false, defaultValue = "") String clientid){
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
    public @ResponseBody byte[] getImage(
            @PathVariable("id") String pFileId,
        @Parameter(description = "Hívó kérés azonosítója", required = false) @RequestHeader(value=CustomRequestLoggingFilter.REQUEST_ID, required = false, defaultValue = "") String clientid) throws FileNotFoundException, IOException{
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
    public ImageProcessModel deleteFiles(
            @PathVariable("id") String pFileId,
        @Parameter(description = "Hívó kérés azonosítója", required = false) @RequestHeader(value=CustomRequestLoggingFilter.REQUEST_ID, required = false, defaultValue = "") String clientid){
        File f = new File(fileDirPath+pFileId);
        if(f.isFile()){
            f.delete();
            return ImageProcessModel.builder().id(pFileId).build();
        }
        throw new BusinessException();
    } 
    
    
    /**
     *  @GetMapping("/sendemail")
    public String sendDemoMail() {
        EmailBean tmp = new EmailBean();
        tmp.setLanguage("hu");
        tmp.setTemplate("assignment");
        tmp.addTarget("karoczkai.krisztian@otpmobil.com");
        Pack p = new Pack();
        p.setString("targy", "narwhal");
        tmp.setParams(p);
        
        try {email.sendMail(tmp);} 
        catch (Exception ex) {
            Logger.getLogger(MicroserviceApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "email sent";
        * 
        * 
        * 
        * 
     * 
     *  Config mock = new ConfigMock();
        Map tmp = new HashMap();
        tmp.put("mq.host", "dev-narwhal.intra.otpmobil.com");
        tmp.put("mq.user", "guest");
        tmp.put("mq.password", "guest");
        tmp.put("mq.topicname", "narwhaldev0");
        tmp.put("mq.bindwith", "foo.bar");
        mock.append(tmp);
        return mock;
     * 
     */
    
    
    
    
    /**
     * 
     * @Component
public class EmailMqClient implements EmailClient {

    private static final String PROPERTY_MQ_HOST = "mq.host";
    private static final String PROPERTY_MQ_USER = "mq.user";
    private static final String PROPERTY_MQ_PASSWORD = "mq.password";
    private static final String PROPERTY_MQ_TOPICNAME = "mq.topicname";
    private static final String PROPERTY_MQ_BINDWITH = "mq.bindwith";

    @Autowired
    private Config config;

    private RabbitTemplate rabbitTemplate;

    private static final Logger logger = LoggerFactory.getLogger(EmailMqClient.class.getName());

    @PostConstruct
    public void init() {
        if (config.get(PROPERTY_MQ_HOST) != null &&
            config.get(PROPERTY_MQ_USER) != null &&
            config.get(PROPERTY_MQ_PASSWORD) != null &&
            config.get(PROPERTY_MQ_TOPICNAME) != null &&
            config.get(PROPERTY_MQ_BINDWITH) != null
        ) {
            this.rabbitTemplate = rabbitTemplate();
            logger.info("Narwal MQ client inited.");
        }
        else {
            logger.info("Narwal MQ client NOT inited.");
        }
    }

    private RabbitTemplate rabbitTemplate() {
        CachingConnectionFactory cf = new CachingConnectionFactory(config.get(PROPERTY_MQ_HOST));
        cf.setUsername(config.get(PROPERTY_MQ_USER));
        cf.setPassword(config.get(PROPERTY_MQ_PASSWORD));

        RabbitTemplate rt = new RabbitTemplate(cf);
        rt.setMessageConverter(new Jackson2JsonMessageConverter());
        return rt;
    }

    @Deprecated
    @Override
    public void sendMail(Pack message) {
        EmailBean eb = new EmailBean(message);
        this.sendMail(eb);
    }

    @Override
    public void sendMail(EmailBean bean) {
        if(rabbitTemplate == null){
            this.init();
        }
        if(rabbitTemplate != null) {
            logger.info("Sending eamilbean to queue.");
            System.out.println(
                    rabbitTemplate.getConnectionFactory().getUsername()+"@"+
                    rabbitTemplate.getConnectionFactory().getHost()+"//"+config.get(PROPERTY_MQ_TOPICNAME).concat("#").concat(config.get(PROPERTY_MQ_BINDWITH).concat(".email")));
            rabbitTemplate.convertAndSend(config.get(PROPERTY_MQ_TOPICNAME), config.get(PROPERTY_MQ_BINDWITH).concat(".email"), bean);
            logger.info("Sended eamilbean to queue.");
        }
        else throw new RuntimeException("MQ client not configured");
    }
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * @Bean
    Queue queue() {
        return new Queue(config.get(PROPERTY_MQ_QUEUENAME), false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(config.get(PROPERTY_MQ_TOPICNAME));
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(config.get(PROPERTY_MQ_BINDWITH).concat(".#"));
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(config.get(PROPERTY_MQ_QUEUENAME));
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, RECIVER_METHOD);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

     */
}
