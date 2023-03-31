package hu.yokudlela.autotests.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import hu.yokudlela.autotests.BaseFeature;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.DecoderException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author krisztian
 */
public class NetworkFunctions extends BaseFeature {


    @When("cleanup")
    public void cleanup() {
        response.clear();
    }

    @When("patch {string}")
    public void patch(String pUrlPath, String docString) throws IOException, InterruptedException, DecoderException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        patch(pUrlPath, "def", docString);
    }

    @When("put {string}")
    public void put(String pUrlPath, String docString) throws IOException, InterruptedException, DecoderException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        put(pUrlPath, "def", docString);
    }


    /*into*/

    @When("patch {string} into {string}")
    public void patch(String pUrlPath, String pResponseId, String docString) throws IOException, InterruptedException, DecoderException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        patchJsonRequest((pResponseId == null || pResponseId.isEmpty())?"def":pResponseId, pUrlPath, docString);
    }

    @When("put {string} into {string}")
    public void put(String pUrlPath, String pResponseId, String docString) throws IOException, InterruptedException, DecoderException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        putJsonRequest((pResponseId == null || pResponseId.isEmpty())?"def":pResponseId, pUrlPath, docString);
    }

    @When("delete {string} into {string}")
    public void delete(String pUrlPath, String pResponseId) throws DecoderException, IOException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        deleteJsonRequest((pResponseId == null || pResponseId.isEmpty())?"def":pResponseId, pUrlPath);
    }

/*auth*/



/**/
    @When("put {string} {string} with external {string}")
    public void putWithExternal(String pResponseId, String pUrlPath, String filePath) throws IOException, DecoderException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String payload = parseJsonFileToString(filePath);
        put(pResponseId, pUrlPath, payload);
    }

    @Given("set header param {string} to {string}")
    public void setHeader(String pKey, String pValue) {
        System.out.println("HEADERS:"+headers.isEmpty());
        super.headers.put(pKey, pValue);
        System.out.println("HEADERS:"+headers.isEmpty());
        System.out.println("HEADERS:"+headers);
    }

    @Then("exists in the response called {string} with external {string}")
    public void responseExternal(String pResponseId, String filePath) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String payload = parseJsonFileToString(filePath);
        response(pResponseId, payload);
    }

    @Then("exists in the response called {string}")
    public void response(String pResponseId, String docString) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, JsonProcessingException {
        HashMap<String, Object> dest = parseJsonStringToMap(docString);

        if (pResponseId.isEmpty()) {
            pResponseId = "def";
        }
        HashMap<String, Object> source = (HashMap) response.get(pResponseId);

        compareMaps(dest, source);
    }



    @Then("exists in token")
    public void token(String docString) throws JsonProcessingException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        HashMap<String, Object> dest = parseJsonStringToMap(docString);
        assert dest.entrySet().stream().allMatch(e ->
                e.getValue().equals("*") ||
                        e.getValue().equals(this.token.get(e.getKey())));
    }

    @Then("exists in the {string} response {string}")
    public void responseHasField(String pResponseId, String pPropertyName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String value = BeanUtils.getProperty(response.get(pResponseId), pPropertyName);
        if (value == null) {
            assertEquals("", "3");
        }
    }

    @Then("exists in response {string}")
    public void responseHasField(String pProperyname) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        responseHasField("def", pProperyname);
    }

    @Then("exists in the {string} response {string} with string value {string}")
    public void responseHasFieldWithStringValue(String pResponseId, String pPropertyName, String expectedValue) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        expectedValue = parse(expectedValue);
        String value = BeanUtils.getProperty(response.get(pResponseId), pPropertyName);
        if (expectedValue.startsWith("\\")) {
            assertEquals(value.matches(expectedValue.substring(1)), Boolean.TRUE.booleanValue());
        } else
            assertEquals(expectedValue, value);
    }

    @Then("exists in response {string} with string value {string}")
    public void responseHasFieldWithStringValue(String pPropertyName, String expectedValue) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        responseHasFieldWithStringValue("def", pPropertyName, expectedValue);
    }


    @Then("response state {int}")
    public void response_state(Integer httpSateCode) {
        assertEquals(httpSateCode, logStatusCode);
    }



    protected void deleteJsonRequest(String id, String pUrlPostFix) throws IOException, InterruptedException, DecoderException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        stepIsNetworkCall = true;
        pUrlPostFix = parse(pUrlPostFix);
        url = getServiceConfig(pUrlPostFix);

        logRequest = requestLogString("DELETE", url, pUrlPostFix, correlationId, null);

        cm.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        HttpClient client = HttpClient.newBuilder()
                .cookieHandler(cm)
                .build();

        HttpRequest request = setRequestHeaders(HttpRequest.newBuilder())
                .uri(URI.create(url.toUrlPrefix() + pUrlPostFix))
                .DELETE()
                .build();


        extractResponse(id, client, request);
    }



    protected void patchJsonRequest(String id, String pUrlPostFix, String pBody) throws IOException, InterruptedException, DecoderException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        pUrlPostFix = parse(pUrlPostFix);
        stepIsNetworkCall = true;
        url = getServiceConfig(pUrlPostFix);
        pBody = parse(pBody);

        logRequest = requestLogString("PATCH", url, pUrlPostFix, correlationId, pBody);

        cm.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        HttpClient client = HttpClient.newBuilder()
                .cookieHandler(cm)
                .build();


        HttpRequest request = setRequestHeaders(HttpRequest.newBuilder())
                .uri(URI.create(url.toUrlPrefix() + pUrlPostFix))
                .method("PATCH", HttpRequest.BodyPublishers.ofString(pBody))
                .build();
        extractResponse(id, client, request);
    }


    protected void putJsonRequest(String id, String pUrlPostFix, String pBody) throws IOException, InterruptedException, DecoderException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        pUrlPostFix = parse(pUrlPostFix);
        stepIsNetworkCall = true;
        url = getServiceConfig(pUrlPostFix);
        pBody = parse(pBody);
        logRequest = requestLogString("PUT", url, pUrlPostFix, correlationId, pBody);

        cm.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        HttpClient client = HttpClient.newBuilder()
                .cookieHandler(cm)
                .build();


        HttpRequest request = setRequestHeaders(HttpRequest.newBuilder())
                .uri(URI.create(url.toUrlPrefix() + pUrlPostFix))
                .PUT(HttpRequest.BodyPublishers.ofString(pBody))
                .build();
        extractResponse(id, client, request);
    }



    private void compareMaps(HashMap<String, Object> dest, HashMap<String, Object> source) {
        dest.forEach((key, value) -> {
            if (value instanceof HashMap) {
                compareMaps((HashMap) value, (HashMap) source.get(key));
            } else {
                assertEquals(value == null ?
                                null : value.toString(),
                        beanUtilWrapper(source, key));
            }
        });
    }

    private String beanUtilWrapper(Object source, String propertyName) {
        try {
            return BeanUtils.getProperty(source, propertyName);
        } catch (Exception e) {
            return null;
        }

    }




    @Before
    public void clearRequestHeaders() {
        this.headers.clear();
    }








}
