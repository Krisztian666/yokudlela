package hu.yokudlela.autotests.ui;

import hu.yokudlela.autotests.BaseFeature;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.beanutils.BeanUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * @author krisztian
 */
public class UIFunctions extends BaseFeature {

    protected static WebDriver driver = null;
    protected static WebDriver driver0 = null;
    protected static WebDriverWait wait = null;
    protected static WebDriverWait wait0 = null;
    protected static String supplier = "";
    private static String attr = "data-test";
    protected int timeoutInSeconds = 20;



    @Given("Open in browser {string}")
    public void init(String pURL) throws MalformedURLException {

        if (("one".equals(System.getProperty("environment.window")) && driver == null)
                || !"one".equals(System.getProperty("environment.window"))) {
            if ("chrome".equals(System.getProperty("environment.browser"))) {
                if (System.getProperty("environment.pluginpath").startsWith("http")) {
                    driver = new RemoteWebDriver(new URL(System.getProperty("environment.pluginpath")), new ChromeOptions());
                } else {
                    System.setProperty("webdriver.chrome.driver", System.getProperty("environment.pluginpath"));
                    driver = new ChromeDriver();
                }
            }

            if ("firefox".equals(System.getProperty("environment.browser"))) {
                if (System.getProperty("environment.pluginpath").startsWith("http")) {
                    driver = new RemoteWebDriver(new URL(System.getProperty("environment.pluginpath")), new FirefoxOptions());
                } else {
                    System.setProperty("webdriver.gecko.driver", System.getProperty("environment.pluginpath"));
                    this.driver = new FirefoxDriver();
                }
            }

            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(timeoutInSeconds, TimeUnit.SECONDS);
        }
        url = getServiceConfig(pURL);
        driver.get(url.toUrlPrefix() + pURL);

    }

    @When("Step {string}")
    public void Step(String string) {
    }

    @When("Click on the {string} button with data-test {string}")
    public void click_on_the_button_with_data_test(String string, String pValue) {
        WebElement element = driver.findElement(By.cssSelector("[" + attr + "=\"" + pValue + "\"]"));

        while (element == null || !element.isEnabled()) {
            try {
                driver.wait(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            element = driver.findElement(By.cssSelector("[" + attr + "=\"" + pValue + "\"]"));
        }

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[" + attr + "=\"" + pValue + "\"]")));
        try {
            driver.findElement(By.cssSelector("[" + attr + "=\"" + pValue + "\"]")).click();
        } catch (Exception e) {
            driver.findElement(By.cssSelector("[" + attr + "=\"" + pValue + "\"] div")).click();
        }
    }

    @When("Click on the {string} element with class {string}")
    public void click_on_the_with_class(String string, String pValue) {
        WebElement element = driver.findElement(By.cssSelector("." + pValue));

        while (element == null || !element.isEnabled()) {
            try {
                driver.wait(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            element = driver.findElement(By.cssSelector("." + pValue));
        }

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("." + pValue)));
        try {
            driver.findElement(By.cssSelector("." + pValue)).click();
        } catch (Exception e) {
            driver.findElement(By.cssSelector("." + pValue)).click();
        }
    }

    @When("Enter {string} in the input field in the data-test {string}")
    public void inputField_InDataTest(String pValue, String dataTest) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (pValue.contains("${jwtcookie")) {
            Cookie cookie1 = driver.manage().getCookieNamed("mt-login-token");
            token = parseJWT(cookie1.getValue());
            pValue = parse(pValue);
        }
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[" + attr + "=\"" + dataTest + "\"] INPUT")));
        driver.findElement(By.cssSelector("[" + attr + "=\"" + dataTest + "\"] INPUT")).clear();
        driver.findElement(By.cssSelector("[" + attr + "=\"" + dataTest + "\"] INPUT")).sendKeys(pValue);
    }

    @When("Enter {string} in the data-test input field {string}")
    public void enter_in_the_input_field_with_data_test(String pValue, String dataTest) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (pValue.contains("${jwtcookie")) {
            Cookie cookie1 = driver.manage().getCookieNamed("mt-login-token");
            token = parseJWT(cookie1.getValue());
            pValue = parse(pValue);
        }
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[" + attr + "=\"" + dataTest + "\"] ")));
        driver.findElement(By.cssSelector("[" + attr + "=\"" + dataTest + "\"] ")).sendKeys(pValue);
    }

    @Then("Check for {string} data-test {string}")
    public WebElement waitForDataTest(String pValue_B, String pValue) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test=\"" + pValue + "\"]")));
        return driver.findElement(By.cssSelector("[data-test=\"" + pValue + "\"]"));
    }

    @Then("{string} exists in {string} element with the value of {string} property {string}")
    public WebElement waitForDataTeststate(String pName, String pDataTest, String pProperty, String pValue) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test=\"" + pDataTest + "\"] [" + pProperty + "*=\"" + pValue + "\"]")));
        return driver.findElement(By.cssSelector("[data-test=\"" + pDataTest + "\"] [" + pProperty + "*=\"" + pValue + "\"]"));
    }

    @Then("Click {string} exists in {string} element with the value of {string} property {string}")
    public void ClickForDataTestinElement(String pName, String pDataTest, String pProperty, String pValue) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test=\"" + pDataTest + "\"] [" + pProperty + "*=\"" + pValue + "\"]")));
        driver.findElement(By.cssSelector("[data-test=\"" + pDataTest + "\"] [" + pProperty + "*=\"" + pValue + "\"]")).click();
    }

    @Then("Click {string} exists in {string} element contains an selector {string}")
    public void ClickForDataTestinSelector(String pName, String pDataTest, String pSelector) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test=\"" + pDataTest + "\"] " + pSelector)));
        driver.findElement(By.cssSelector("[data-test=\"" + pDataTest + "\"] " + pSelector)).click();
    }

    @Then("Click on element type {string} having attribute {string} with value {string}")
    public void ClickForDataTestinElement(String pName, String pAttribute, String pValue) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[" + pAttribute + "=\"" + pValue + "\"]")));
        driver.findElement(By.cssSelector("[" + pAttribute + "=\"" + pValue + "\"]")).click();
    }

    @Then("Enter {string} exists in {string} element with the value of {string} property {string}")
    public void EnterForDataTestinElement(String pValuee, String pDataTest, String pProperty, String pValue) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test=\"" + pDataTest + "\"] [" + pProperty + "*=\"" + pValue + "\"]")));
        driver.findElement(By.cssSelector("[data-test=\"" + pDataTest + "\"] [" + pProperty + "*=\"" + pValue + "\"] INPUT")).clear();
        driver.findElement(By.cssSelector("[data-test=\"" + pDataTest + "\"] [" + pProperty + "*=\"" + pValue + "\"] INPUT")).sendKeys(pValuee);
    }

    @Then("Value {string} exists in {string} element with the value of {string} property {string}")
    public void ValueForDataTestinElement(String pValuee, String pDataTest, String pProperty, String pValue) {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test=\"" + pDataTest + "\"] [" + pProperty + "*=\"" + pValue + "\"]")));
        String dataErtek = driver.findElement(By.cssSelector("[data-test=\"" + pDataTest + "\"] [" + pProperty + "*=\"" + pValue + "\"]")).getAttribute("outerText");
        System.out.println("dataErtek:'" + dataErtek + "'");
        System.out.println("pValuee:'" + pValuee + "'");
        assert (pValuee.trim().equals(dataErtek.trim()));
    }

    @Then("Value {string} of in {string} = {string}")
    public void ValueDataTestForinElement(String pValuee, String pClass, String pValue) {
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[" + pClass + "=\"" + pValue + "\"]")));
        String dataErtek = driver.findElement(By.cssSelector("[" + pClass + "=\"" + pValue + "\"]")).getAttribute("outerText");
        if (dataErtek == null) {
            dataErtek = driver.findElement(By.cssSelector("[" + pClass + "=\"" + pValue + "\"]")).getAttribute("innerText");
        }
        System.out.println("dataErtek:'" + dataErtek + "'" + "[" + pClass + "=\"" + pValue + "\"]");
        System.out.println("pValuee:'" + pValuee + "'");
        assert (pValuee.trim().equals(dataErtek.trim()));
    }

    @When("Cookie exists {string}")
    public void existsCookie(String pValue) throws InterruptedException {
        Cookie cookie1 = driver.manage().getCookieNamed(pValue);
        assert (cookie1 != null);
    }

    protected WebElement waitForAriaTest(String pValue) {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[aria-invalid=\"" + pValue + "\"]")));
        return driver.findElement(By.cssSelector("[aria-invalid=\"" + pValue + "\"]"));
    }

    protected WebElement waitForSelectableDataTest(String pValue) {
        wait.until(ExpectedConditions.elementSelectionStateToBe(By.cssSelector("[data-test=\"" + pValue + "\"]"), false));
        return driver.findElement(By.cssSelector("[data-test=\"" + pValue + "\"]"));
    }

    @When("Exist in token")
    public synchronized void printToken() throws InterruptedException {

        Cookie cookie1 = driver.manage().getCookieNamed("mt-login-token");
        System.out.println(parseJWT(cookie1.getValue()));
    }

    @When("Wait for {string}")
    public void wait_for(String string) {
        WebElement element = driver.findElement(By.cssSelector(string));
        while (element == null && !element.isEnabled()) {
            try {
                driver.wait(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            element = driver.findElement(By.cssSelector(string));
        }
    }

    @When("Set the {string} cookie value to {string}")
    public void wait_for(String pCookie, String pValue) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Cookie cookie = new Cookie(pCookie, parse(pValue));
        driver.manage().addCookie(cookie);
    }


    private String resolveJwtCookieParam(String docString) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String doc;
        while (docString.contains("${jwtcookie(")) {
            int idx0 = docString.indexOf("${jwtcookie(");
            int idx1 = docString.indexOf(")", idx0);
            int idx2 = docString.indexOf("}", idx1);
            String cookieName = docString.substring(idx0 + "${jwtcookie(".length(), idx1);
            Cookie cookie1 = driver.manage().getCookieNamed(cookieName);
            Map<String, Object> tmp0 = parseJWT(cookie1.getValue());
            String value = BeanUtils.getProperty(tmp0, docString.substring(idx1 + 2, idx2));
            doc = docString.substring(0, idx0).concat(value).concat(docString.substring(idx2 + 1));
            docString = doc;
        }
        System.out.println("TMP0:" + docString);
        return docString;
    }
    }
