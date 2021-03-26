package ru.netology;

import org.junit.jupiter.api.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Selenium {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        if (System.getProperty("os.name").contains("Windows")) {
            System.setProperty("webdriver.chrome.driver", "./Driver/chromedriver.exe");
        } else if (System.getProperty("os.name").contains("Linux")) {
            System.setProperty("webdriver.chrome.driver", "Driver/chromedriver");
        }
    }

    @BeforeEach
    void setUp() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(true);
        driver = new ChromeDriver(chromeOptions);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSendForm() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79999999999");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector("[type=button]")).submit();
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldShowErrorName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ivan Ivanov");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79999999999");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector("[type=button]")).submit();
        String text = driver.findElement(By.cssSelector(".input_invalid[data-test-id=name] .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldShowErrorBlankName() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys(" 7999999999в");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector("[type=button]")).submit();
        String text = driver.findElement(By.cssSelector(".input_invalid[data-test-id=name] .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldShowErrorPhone() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys(" 7999999999в");
        driver.findElement(By.cssSelector("[data-test-id=agreement] .checkbox__box")).click();
        driver.findElement(By.cssSelector("[type=button]")).submit();
        String text = driver.findElement(By.cssSelector(".input_invalid[data-test-id=phone] .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void shouldShowErrorBlankPhone() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("");
        driver.findElement(By.cssSelector("[type=button]")).submit();
        String text = driver.findElement(By.cssSelector(".input_invalid[data-test-id=phone] .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void shouldNotSubmitUncheckedChekbox() {
        driver.get("http://localhost:9999");
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79999999999");
        driver.findElement(By.cssSelector("[type=button]")).submit();
        String text = driver.findElement(By.cssSelector(".input_invalid[data-test-id=agreement] .checkbox__text")).getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", text.trim());
    }

}
