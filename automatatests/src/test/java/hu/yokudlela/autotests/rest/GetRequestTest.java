package hu.yokudlela.autotests.rest;

import hu.yokudlela.autotests.BaseFeature;
import io.cucumber.java.en.When;
import org.apache.commons.codec.DecoderException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class GetRequestTest extends BaseFeature {

    @When("get {string}")
    public void get(String pUrlPath) throws IOException, InterruptedException, DecoderException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        get(pUrlPath,"def");
    }

    @When("get {string} into {string}")
    public void get(String pUrlPath, String pResponseId) throws IOException, InterruptedException, DecoderException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        getJsonRequest((pResponseId == null || pResponseId.isEmpty())?"def":pResponseId, pUrlPath);
    }

    protected void getJsonRequest(String id, String pUrlPostFix) throws IOException, InterruptedException, DecoderException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        stepIsNetworkCall = true;
        pUrlPostFix = parse(pUrlPostFix);
        url = getServiceConfig(pUrlPostFix);

        logRequest = requestLogString("GET", url, pUrlPostFix, correlationId, null);

        cm.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        HttpClient client = HttpClient.newBuilder()
                .cookieHandler(cm)
                .build();

        HttpRequest request = setRequestHeaders(HttpRequest.newBuilder())
                .uri(URI.create(url.toUrlPrefix() + pUrlPostFix))
                .GET()
                .build();


        extractResponse(id, client, request);
    }



}
