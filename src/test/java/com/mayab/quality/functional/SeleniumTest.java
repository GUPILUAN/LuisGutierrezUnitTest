package com.mayab.quality.functional;

import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class SeleniumTest {
    private WebDriver driver;
    private String baseUrl;
    // private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    JavascriptExecutor js;

    @Before
    public void setUp() throws Exception {
        WebDriverManager.chromedriver().setup();
        // System.setProperty("webdriver.chrome.driver", "");
        driver = new ChromeDriver();
        baseUrl = "https://www.google.com/";
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void testUntitledTestCase() throws Exception {
        driver.get(baseUrl + "chrome://newtab/");
        driver.get("https://www.google.com/");
        driver.findElement(By.id("APjFqb")).clear();
        driver.findElement(By.id("APjFqb")).sendKeys("kittens");
        driver.findElement(
                By.xpath("//img[contains(@src,'https://www.google.com/logos/fnbx/animal_paws/cat_kp_dm.gif')]"))
                .click();
    }

    @After
    public void tearDown() throws Exception {
        TimeUnit.SECONDS.sleep(5);
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    /*
     * private boolean isElementPresent(By by) {
     * try {
     * driver.findElement(by);
     * return true;
     * } catch (NoSuchElementException e) {
     * return false;
     * }
     * }
     * 
     * private boolean isAlertPresent() {
     * try {
     * driver.switchTo().alert();
     * return true;
     * } catch (NoAlertPresentException e) {
     * return false;
     * }
     * }
     * 
     * private String closeAlertAndGetItsText() {
     * try {
     * Alert alert = driver.switchTo().alert();
     * String alertText = alert.getText();
     * if (acceptNextAlert) {
     * alert.accept();
     * } else {
     * alert.dismiss();
     * }
     * return alertText;
     * } finally {
     * acceptNextAlert = true;
     * }
     * }
     */

}
