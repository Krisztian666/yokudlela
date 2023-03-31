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

public class PostRequestTest extends BaseFeature {
    @When("post {string}")
    public void post(String pUrlPath, String docString) throws IOException, InterruptedException, DecoderException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        post(pUrlPath, "def", docString);
    }

    @When("form post {string}")
    public void formPost(String pUrlPath, String docString) throws IOException, InterruptedException, DecoderException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        formPost(pUrlPath,"def", docString);
    }

    @When("post {string} into {string}")
    public void post(String pUrlPath, String pResponseId, String docString) throws IOException, InterruptedException, DecoderException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        postJsonRequest((pResponseId == null || pResponseId.isEmpty())?"def":pResponseId, pUrlPath, docString);
    }

    @When("form post {string} into {string}")
    public void formPost(String pUrlPath, String pResponseId, String docString) throws IOException, InterruptedException, DecoderException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        postFormRequest((pResponseId == null || pResponseId.isEmpty())?"def":pResponseId, pUrlPath, docString);
    }

    @When("post {string} with external {string} into {string}")
    public void postWithExternalIfNotExists(String pUrlPath, String filePath, String pResponseId) throws IOException, DecoderException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String payload = parseJsonFileToString(filePath);
        post(pResponseId, pUrlPath, payload);
    }

    @When("post {string} {string} with external {string} generate email")
    public void postWithExternalGenerateEmail(String pResponseId, String pUrlPath, String filePath) throws IOException, DecoderException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String payload = parseJsonFileToString(filePath);
        String emailField = "\"email\": ";
        int idx0 = payload.indexOf(emailField);
        if(idx0 == -1) {
            throw new RuntimeException("Payload does not contain \"email\" field.");
        }
        int idx1 = payload.indexOf(",", idx0);
        String email = "\"" + generateRandomString() + "@" + generateRandomString() + ".tk" + "\"";
        payload = payload.substring(0, idx0 + emailField.length()).concat(email).concat(payload.substring(idx1));
        post(pResponseId, pUrlPath, payload);
    }



    private void postFormRequest(String id, String pUrlPostFix, String pBody) throws IOException, InterruptedException, DecoderException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
        stepIsNetworkCall = true;
        url = getServiceConfig(pUrlPostFix);
        pUrlPostFix = parse(pUrlPostFix);
        pBody = parse(pBody);

        logRequest = requestLogString("POST", url, pUrlPostFix, correlationId, pBody);

        cm.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        HttpClient client = HttpClient.newBuilder()
                .cookieHandler(cm)
                .build();

        HttpRequest request = setRequestHeaders(HttpRequest.newBuilder())
                .uri(URI.create(url.toUrlPrefix() + pUrlPostFix))
                .setHeader("Content-Type","application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(pBody))
                .build();
/*        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        for(HttpCookie cookie:cm.getCookieStore().getCookies()){
            if("mt-login-token".equals(cookie.getName())){
                parseJWT(cookie.getValue());
            }
        }
*/

        extractResponse(id, client, request);
    }


    private void postJsonRequest(String id, String pUrlPostFix, String pBody) throws IOException, InterruptedException, DecoderException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        pUrlPostFix = parse(pUrlPostFix);
        stepIsNetworkCall = true;
        url = getServiceConfig(pUrlPostFix);
        pBody = parse(pBody);

        logRequest = requestLogString("POST", url, pUrlPostFix, correlationId, pBody);

        cm.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        HttpClient client = HttpClient.newBuilder()
                .cookieHandler(cm)
                .build();


        HttpRequest request = setRequestHeaders(HttpRequest.newBuilder())
                .uri(URI.create(url.toUrlPrefix() + pUrlPostFix))
                .POST(HttpRequest.BodyPublishers.ofString(pBody))
                .build();
        extractResponse(id, client, request);
    }

}
