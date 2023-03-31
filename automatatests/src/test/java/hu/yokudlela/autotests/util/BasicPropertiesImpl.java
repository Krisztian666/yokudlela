package hu.yokudlela.autotests.util;

import com.rabbitmq.client.BasicProperties;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;
@Data
@NoArgsConstructor
public class BasicPropertiesImpl implements BasicProperties {

    @Builder
    public BasicPropertiesImpl(String contentType, String contentEncoding, Map<String, Object> headers, Integer deliveryMode, Integer priority, String correlationId, String replyTo, String expiration, String messageId, Date timestamp, String type, String userId, String appId) {
        this.contentType = contentType;
        this.contentEncoding = contentEncoding;
        this.headers = headers;
        this.deliveryMode = deliveryMode;
        this.priority = priority;
        this.correlationId = correlationId;
        this.replyTo = replyTo;
        this.expiration = expiration;
        this.messageId = messageId;
        this.timestamp = timestamp;
        this.type = type;
        this.userId = userId;
        this.appId = appId;
    }

    String contentType;

    String contentEncoding;

    Map<String, Object> headers;

    Integer deliveryMode;

    Integer priority;

    String correlationId;

    String replyTo;

    String expiration;

    String messageId;

    Date timestamp;

    String type;

    String userId;

    String appId;
}
