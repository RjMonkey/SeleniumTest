import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;
import static org.testng.Assert.assertEquals;



@RunWith(Parameterized.class)
public class SeleniumSuite {

    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private String number;
    private String password;
    private String excepted;


    public SeleniumSuite(String number, String password, String excepted){
        this.number = number;
        this.password = password;
        this.excepted = excepted;
    }

    @Before
    public void setUp() throws Exception {
        driver = new FirefoxDriver();
        baseUrl = "http://121.193.130.195:8080/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testSeleniumSuite() throws Exception {
        driver.get(baseUrl);
        driver.findElement(By.id("name")).clear();
        driver.findElement(By.id("name")).sendKeys(this.number);
        driver.findElement(By.id("pwd")).clear();
        driver.findElement(By.id("pwd")).sendKeys(this.password);
        driver.findElement(By.id("submit")).click();

        String result = driver.findElement(By.id("resultString")).getAttribute("innerHTML").trim();
        assertEquals(this.excepted, result);
    }
    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }


    @Parameterized.Parameters
    public static Collection<Object[]> testData() throws FileNotFoundException, IOException {
        String inputFilePath = new File(System.getProperty("user.dir")).getParent();
        File inputFile = new File(inputFilePath + "/inputgit.csv");
        Collection<Object[]> list = new ArrayList<Object[]>();

        if(inputFile.exists()){
            System.out.println("ok");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "GBK"));
            reader.readLine();

            while(true){
                String str = reader.readLine();
                if(str == null) break;

                String[] strList = str.split(",");
                String number = strList[0];
                String password = number.substring(4);
                strList[0] = strList[1];
                strList[1] = number;
                String expected = String.join(",", strList);
                list.add(new String[]{number, password, expected});
            }
        }
        return list;
    }


    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}

