package Tests;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class TestCaseSetup {
    public static String readProperty(String property) {
        Properties prop;
        String value = null;
        try {
            prop = new Properties();
            prop.load(new FileInputStream(new File("config.properties")));
            value = prop.getProperty(property);
            if (value == null || value.isEmpty()) {
                throw new Exception("Value not set or empty");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
    public static AppiumDriver<?> getDriver() {
        AppiumDriver<?> driver;
        DesiredCapabilities caps = new DesiredCapabilities();
        if (Boolean.parseBoolean(TestCaseSetup.readProperty("run.hybrid"))) {
            caps.setCapability(MobileCapabilityType.AUTO_WEBVIEW, true);
        }
        String completeURL = "http://" + TestCaseSetup.readProperty("run.ip") + ":" + TestCaseSetup.readProperty("run.port") + "/wd/hub";
        String platform = System.getProperty("platformName");
        switch (platform) {
            case "IOS":
                caps.setCapability("deviceName", System.getProperty("deviceName"));
                caps.setCapability("automationName", System.getProperty("automationName"));
                caps.setCapability("platformName", System.getProperty("platformName"));
               // caps.setCapability("platformVersion", System.getProperty("platformVersion"));
                caps.setCapability("udid", System.getProperty("udid"));
                //caps.setCapability("noReset", System.getProperty("noReset"));
                //caps.setCapability("automationName", System.getProperty("automationName"));
                caps.setCapability("app", System.getProperty("app"));
                caps.setCapability("platformVersion", System.getProperty("platformVersion"));
                //caps.setCapability("bootstrapPath", System.getProperty("bootstrapPath"));
                //caps.setCapability("agentPath", System.getProperty("agentPath"));
                driver = new IOSDriver<RemoteWebElement>(toUrl(completeURL), caps);
                break;
            case "Android":
                caps.setCapability("deviceName", System.getProperty("deviceName"));
                caps.setCapability("platformName", System.getProperty("platformName"));
                caps.setCapability("platformVersion", System.getProperty("platformVersion"));
                caps.setCapability("appPackage", System.getProperty("appPackage"));
                caps.setCapability("appActivity", System.getProperty("appActivity"));
                caps.setCapability("app", System.getProperty("app"));
                caps.setCapability("app", System.getProperty("udid"));
                //caps.setCapability("noReset", System.getProperty("noReset"));
                //caps.setCapability("fullReset", System.getProperty("fullReset"));
                //caps.setCapability("skipDeviceInitialization", System.getProperty("skipDeviceInitialization"));
                //caps.setCapability("skipServerInstallation", System.getProperty("skipServerInstallation"));
                //caps.setCapability("dontStopAppOnReset", System.getProperty("dontStopAppOnReset"));
                //caps.setCapability("autoAcceptAlerts",System.getProperty("autoAcceptAlerts"));
                //caps.setCapability("androidInstallTimeout",System.getProperty("androidInstallTimeout"));
                driver = new AndroidDriver<RemoteWebElement>(toUrl(completeURL), caps);   //Local
                break;
            default:
                throw new IllegalArgumentException(String.format("Platform %s not supported", platform));
        }
        return driver;
    }
    private static URL toUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(String.format("Url [%s] is malformed", url));
        }
    }
}