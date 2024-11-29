package com.mayab.quality.functional;

import org.apache.commons.io.FileUtils;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);
        baseUrl = "https://www.google.com/";
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void testUntitledTestCase() throws Exception {
        driver.get(baseUrl + "chrome://newtab/");

        /*
         * driver.get("https://www.google.com/");
         * driver.findElement(By.id("APjFqb")).clear();
         * driver.findElement(By.id("APjFqb")).sendKeys("kittens");
         * driver.findElement(By.id("APjFqb")).sendKeys(Keys.RETURN);
         * driver.findElement(
         * By.xpath(
         * "//img[contains(@src,'https://www.google.com/logos/fnbx/animal_paws/cat_kp_dm.gif')]"
         * ))
         * .click();
         * TimeUnit.SECONDS.sleep(5);
         * driver.findElement(By.xpath(
         * "/html/body/div[4]/div/div[12]/div[1]/div[2]/div[2]/div/div/div[2]/div[7]/div/div/div[1]/div/div/span/a"
         * ))
         * .click();
         */

        driver.get("https://www.facebook.com/");
        driver.findElement(By.id("email")).sendKeys("test@email.com");
        driver.findElement(By.id("pass")).sendKeys("test123");
        driver.findElement(By.name("login")).click();
        TimeUnit.SECONDS.sleep(5);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            WebElement element = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[normalize-space(text())='The password you’ve entered is incorrect.']")));

            System.out.println("Elemento encontrado: " + element.getText());
            String actualResult = element.getText();
            System.out.println(actualResult);

            Assert.assertEquals("The password you’ve entered is incorrect.\n" + //
                    "Forgot Password?".strip(), actualResult.strip());

        } catch (TimeoutException e) {
            System.out.println("No se encontró el elemento dentro del tiempo de espera.");
            takeScreenShot("facebookError");
            throw e;
        }

    }

    public void takeScreenShot(String fileName) throws IOException {

        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        OutputStream outPath = new FileOutputStream("src/screenshots/" + fileName + ".jpeg");
        FileUtils.copyFile(file, outPath);

    }

    @After
    public void tearDown() throws Exception {

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
