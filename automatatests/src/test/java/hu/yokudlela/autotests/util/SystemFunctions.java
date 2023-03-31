package hu.yokudlela.autotests.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import hu.yokudlela.autotests.BaseFeature;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import org.junit.Before;

import java.util.UUID;

/**
 * @author krisztian
 */
public class SystemFunctions extends BaseFeature {

    public SystemFunctions() {
        super();
    }

    @Before
    public void createNewCorrelationId() {
        super.correlationId = UUID.randomUUID().toString();
        super.logRequest = null;
        super.logResponse = null;
        super.logStatusCode = 0;
    }

    @BeforeStep
    public void clearNetworkLogs() {
        stepIsNetworkCall = false;
    }


    @AfterStep
    public void afterStepScenario(Scenario scenario) throws CloneNotSupportedException {
        if (stepIsNetworkCall) {
            scenario.attach("Request:" + super.logRequest, "text/html", "request(" + scenario.getName() + ")");
            scenario.attach("Response sate code:" + super.logStatusCode, "text/html", "state");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(super.logResponse.replaceAll("\n", ""));
            String prettyJsonString = gson.toJson(je);
            scenario.attach("Response:\n" + prettyJsonString, "text/html", "response");
        }
        if (scenario.isFailed()) {
        }

    }

    @After
    public void afterScenario(Scenario scenario) {
    }

    @Given("Fut a service")
    public void fut_a_service() {
        // ! Write code here that turns the phrase above into concrete actions
    }

    @Given("set config {string} {string} {string} {string} {string} {string} {string}")
    public void set_config(String contact, String protocol, String domain, String api, String key, String channelHeader, String correlationHeader) {
        this.serviceConfigs.put(api, new ServiceConfig(contact, domain, api, key, protocol, channelHeader, correlationHeader));
    }


}
