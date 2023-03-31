package hu.yokudlela.autotests;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        tags = "@reservation and @local",
        features= "src/test/resources/hu.yokudlela.autotests",
//        glue = "hu.yokudlela.autotests",
        plugin = {"pretty","html:target/cucumber-reports.html", "json:target/cucumber.json"}
)
public class ApiTests {
}
