import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;
import java.util.Random;

public class BasicTestSetup {

    private static AppiumDriver driver;
    public static void main(String[] args) {
        try {
            setUp();
            testMethod();
            tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setUp() throws Exception {

        File appDir = new File("F:\\");
        File app = new File(appDir, "SportTimer.apk");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("device","DcherDeviceName");

        //mandatory capabilities
        capabilities.setCapability("deviceName","DcherDeviceName");
        capabilities.setCapability("platformName","android");

        //other caps
        capabilities.setCapability("app", app.getAbsolutePath());
        driver =  new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        System.out.println("Connected");
    }

    public static String getCurrentTimeString(String number, String action) {
        long currentTime = System.currentTimeMillis();//+ TimeZone.getDefault().getRawOffset();
        long days = currentTime/24000/3600;
        currentTime -= days*24000*3600;
        long hours = currentTime/3600000;
        currentTime -= hours*3600000;
        long minutes = currentTime/60000;
        currentTime -= minutes*60000;
        long seconds = currentTime/1000;
        currentTime -= seconds*1000;
        long msFirst = currentTime/100;
        long msSecond = currentTime/10%10;
        long msThird = currentTime%10;
        return number + "#" + days + " " + hours+
                ":"+minutes+":"+
                seconds+"."+msFirst+msSecond+msThird + "#" + action +"##\n";
    }

    public static void testMethod() {
        String action = "testAction";
        String number = new Integer(new Random().nextInt(9999)).toString();
        By competitorId = By.id("editTextNextNumber");
        By actionName = By.id("editTextEventDescription");
        By resultsList = By.id("editTextResults");

        driver.findElement(actionName).clear();
        driver.findElement(competitorId).clear();
        driver.findElement(resultsList).clear();

        driver.findElement(actionName).sendKeys(action);
        driver.findElement(competitorId).sendKeys(number + "\n");
        String expectedString = getCurrentTimeString(number, action);
        String actualResult = driver.findElement(resultsList).getText();
        System.out.println("Expected:");
        System.out.println(expectedString);
        System.out.println(actualResult);
        System.out.println("- actual");
        System.out.println("Verdict: " + actualResult.replaceAll("\\.(\\d)*#", "#").contains(expectedString.replaceAll("\\.(\\d)*#", "#")));
 }

    public static void tearDown(){
        driver.quit();
        System.out.println("Session closed");
    }

}