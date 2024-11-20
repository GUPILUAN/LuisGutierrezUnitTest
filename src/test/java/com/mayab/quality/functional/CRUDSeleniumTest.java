package com.mayab.quality.functional;

import org.junit.*;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

//Tests will execute in order
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CRUDSeleniumTest {
    private WebDriver driver;
    private String baseUrl;
    // private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    JavascriptExecutor js;

    // setup
    @Before
    public void setUp() throws Exception {
        WebDriverManager.chromedriver().setup();
        // System.setProperty("webdriver.chrome.driver", "");
        driver = new ChromeDriver();
        baseUrl = "https://www.google.com/";
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        js = (JavascriptExecutor) driver;
    }

    // create a new record
    @Test
    public void test1_createNewRecord() throws Exception {
        driver.get(baseUrl + "chrome://newtab/");
        driver.get("https://mern-crud-mpfr.onrender.com");
        driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/button")).click();
        driver.findElement(By.name("name")).sendKeys("test luis");
        driver.findElement(By.name("email")).sendKeys("testluis@email.com");
        driver.findElement(By.name("age")).sendKeys("24");
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div")).click();
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/div[2]/div[1]")).click();
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/button")).click();
        TimeUnit.SECONDS.sleep(2);
        String successMessage = driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[4]/div/div"))
                .getText();

        String success = driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[4]/div/p")).getText();

        System.out.println(success);
        assertEquals("Nice one!", successMessage);
        assertEquals("Successfully added!", success);

        driver.findElement(By.xpath("/html/body/div[3]/div/i")).click();
        TimeUnit.SECONDS.sleep(2);

    }

    // trying to create a new record but fails cause the email is already registered
    @Test
    public void test2_createExistingEmail() throws Exception {
        driver.get(baseUrl + "chrome://newtab/");
        driver.get("https://mern-crud-mpfr.onrender.com");
        driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/button")).click();
        driver.findElement(By.name("name")).sendKeys("new test name");
        // this email is already registered in the previuous test
        driver.findElement(By.name("email")).sendKeys("testluis@email.com");
        driver.findElement(By.name("age")).sendKeys("24");
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div")).click();
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[3]/div[2]/div/div[2]/div[1]")).click();
        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/button")).click();
        TimeUnit.SECONDS.sleep(2);
        String noSuccessMessage = driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[5]/div/div"))
                .getText();

        String noSuccess = driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[5]/div/p")).getText();

        System.out.println(noSuccess);
        assertEquals("Woah!", noSuccessMessage);
        assertEquals("That email is already taken.", noSuccess);

        TimeUnit.SECONDS.sleep(2);

    }

    // test edit record
    @Test
    public void test3_editRecord() throws Exception {
        driver.get(baseUrl + "chrome://newtab/");
        driver.get("https://mern-crud-mpfr.onrender.com");
        TimeUnit.SECONDS.sleep(2);
        WebElement tabla = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table"));
        WebElement body = tabla.findElement(By.tagName("tbody"));
        List<WebElement> filas = body.findElements(By.tagName("tr"));

        filas.forEach(fila -> {
            List<WebElement> columnas = fila.findElements(By.tagName("td"));
            columnas.forEach(columna -> {
                // the record tha will be edited by its email
                if (columna.getText().equals("testluis@email.com")) {
                    WebElement boton = fila.findElement(By.className("blue"));
                    boton.click();
                    try {
                        TimeUnit.SECONDS.sleep(2);
                        driver.findElement(By.name("age")).sendKeys(Keys.BACK_SPACE);
                        driver.findElement(By.name("age")).sendKeys(Keys.BACK_SPACE);
                        driver.findElement(By.name("age")).sendKeys("30");
                        driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/form/button")).click();
                        TimeUnit.SECONDS.sleep(2);
                        String successMessage = driver
                                .findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[4]/div/div"))
                                .getText();

                        String success = driver
                                .findElement(By.xpath("/html/body/div[3]/div/div[2]/form/div[4]/div/p"))
                                .getText();

                        assertEquals("Nice one!", successMessage);
                        assertEquals("Successfully updated!", success);

                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        System.out.println("ERROR EN EDIT");
                        e.printStackTrace();
                    }

                }
            });
        });

    }

    // finding a record with a given name
    @Test
    public void test4_getByName() throws Exception {
        // Name you are looking for
        String nameWanted = "Test Luis";
        driver.get(baseUrl + "chrome://newtab/");
        driver.get("https://mern-crud-mpfr.onrender.com");
        TimeUnit.SECONDS.sleep(2);
        WebElement tabla = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table"));

        WebElement body = tabla.findElement(By.tagName("tbody"));
        List<WebElement> filas = body.findElements(By.tagName("tr"));

        List<List<String>> datosTabla = new ArrayList<>();

        filas.forEach(fila -> {
            List<WebElement> columnas = fila.findElements(By.tagName("td"));
            List<String> datosFila = new ArrayList<>();

            columnas.forEach(columna -> {
                if (!columna.getText().equals("EditDelete")) {
                    datosFila.add(columna.getText());
                }
            });

            if (datosFila.get(0).equals(nameWanted)) {
                datosTabla.add(datosFila);
            }

        });

        System.out.println("Buscando 'Test Luis': ");
        System.out.println(datosTabla);

        datosTabla.forEach(dato -> {
            assertEquals(nameWanted, dato.get(0));
        });
        assertTrue(datosTabla.size() >= 1);
    }

    // print all records and checks if the table is not empty
    @Test
    public void test5_getAll() throws Exception {
        driver.get(baseUrl + "chrome://newtab/");
        driver.get("https://mern-crud-mpfr.onrender.com");
        TimeUnit.SECONDS.sleep(2);
        WebElement tabla = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table"));

        WebElement body = tabla.findElement(By.tagName("tbody"));
        List<WebElement> filas = body.findElements(By.tagName("tr"));

        List<List<String>> datosTabla = new ArrayList<>();

        filas.forEach(fila -> {
            List<WebElement> columnas = fila.findElements(By.tagName("td"));
            List<String> datosFila = new ArrayList<>();

            columnas.forEach(columna -> {
                if (!columna.getText().equals("EditDelete")) {
                    datosFila.add(columna.getText());
                }
            });
            datosTabla.add(datosFila);
        });

        System.out.println("Todos los datos de la tabla: ");
        System.out.println(datosTabla);
        assertFalse(datosTabla.size() == 0);
    }

    // delete a record
    @Test
    public void test6_deleteRecord() throws Exception {
        driver.get(baseUrl + "chrome://newtab/");
        driver.get("https://mern-crud-mpfr.onrender.com");
        TimeUnit.SECONDS.sleep(2);
        WebElement tabla = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/table"));
        WebElement body = tabla.findElement(By.tagName("tbody"));
        List<WebElement> filas = body.findElements(By.tagName("tr"));

        filas.forEach(fila -> {
            List<WebElement> columnas = fila.findElements(By.tagName("td"));
            columnas.forEach(columna -> {
                // the record that will be eliminated by its email
                if (columna.getText().equals("testluis@email.com")) {
                    WebElement boton = fila.findElement(By.className("black"));
                    assertNotNull(boton);
                    boton.click();
                    try {
                        TimeUnit.SECONDS.sleep(2);
                        driver.findElement(By.xpath("/html/body/div[3]/div/div[3]/button[1]")).click();

                        // exit the funtion to avoid an error if the record deleted was the only record
                        // in the table
                        return;

                    } catch (InterruptedException e) {
                        System.out.println("ERROR EN DELETE");
                        e.printStackTrace();
                    }

                }
            });
        });

    }

    // tear down
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
