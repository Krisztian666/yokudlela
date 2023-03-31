package hu.yokudlela.autotests.util;

/**
 *
 * @author krisztian
 */
public class ServiceConfig {
    public String domain;
    public String api;
    public String key;
    public String protocol;
    public String channel;
    public String context;
    public String contact;

    public ServiceConfig() {
    }
    
    public ServiceConfig(String contact, String domain, String api, String key, String protocol, String channel, String context) {
        this.contact = contact;
        this.domain = domain;
        this.api = api;
        this.key = key;
        this.protocol = protocol;
        this.channel = channel;
        this.context = context;
    }
    
    public String toUrlPrefix() {
        return this.protocol.concat("://").concat(domain);
    }
    
}

