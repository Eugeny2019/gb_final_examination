package ru.gb_test.evch;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.io.*;
import java.util.Properties;
import java.util.Random;

//ToDo после исправления валидации (сейчас вместо валидации отправляется запрос на бэк),
// добавить проверку на наличие текста валидации (см.таблицу ФЛК полей 1.1 Авторизация).
// Сейчас нельзя добавить, т.к. не известны селекторы.

public class LoginPageTest {
    WebDriver driver;
    MainPage mainPage;
    static Properties prop = new Properties();
    private static String url;
    private static String bigString;

    @RegisterExtension
    TestWatcher testWatcher = new JUnitExtention();

    @BeforeAll
    static void registerDriver() throws IOException {
        WebDriverManager.chromedriver().setup();
        prop.load(new FileInputStream("src/main/resources/my.properties"));
        url = prop.getProperty("url");
        bigString = prop.getProperty("bigString");

    }

    @BeforeEach
    public void initDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new EventFiringDecorator(new AdditionalLogger()).decorate(new ChromeDriver(options));
        driver.manage().window().setSize(new Dimension(1500, 900));
        driver.get(url);
        mainPage = new MainPage(driver);
    }

    //Field Login
    @Test
    @Feature("Тестирование поля логин. Позитивная. Ввод валидных credentials и переход на страницу Blog. Проверка на латинские символы и цифры")
    public void loginFieldPositiveValidAuthTest() {
        BlogPage blogPage = mainPage.gotoLoginPage()
                .setLoginFieldValidText()
                .setPasswordFieldValidText()
                .clickLoginButtonForValidCredetials();
        System.out.println(blogPage.driver.getPageSource());
        Assertions.assertTrue(blogPage.driver.getPageSource().contains("Blog"));
    }

    @Deprecated
    @Test
    @Feature("Тестирование поля логин. Негативная. ввести латинские символы, цифры, спецсимволы")
    public void loginFieldNegativeWithSpecialSimbolsTest() {
        LoginPage loginPage = mainPage.gotoLoginPage()
                .setLoginFieldText(generatingString(2, 14))
                .setPasswordFieldValidText()
                .clickLoginButtonForInvalidCredetials();

        String loginField = "//label['.mdc-text-field--filled']";
        String color1 = loginPage.driver.findElement(By.xpath(loginField)).getCssValue("background-color");
        System.out.println("rgba(245, 245, 245, 1)".equals(color1));
        Assertions.assertTrue(!"rgba(245, 245, 245, 1)".equals(color1) || "rgba(248, 227, 221, 0)".equals(color1)); //здесь с альфой не понятно, пикет не показывает на макете альфу.

        String errorField = "//div[contains(@class,\"error-block\")]";
        Assertions.assertFalse(loginPage.driver.findElement(By.xpath(errorField)).isDisplayed());
    }

    @Deprecated
    @Test
    @Feature("Тестирование поля логин. Негативная. ввести не латинские символы (кириллица)")
    public void loginFieldNegativeWithCirillicSimbolsTest() {
        LoginPage loginPage = mainPage.gotoLoginPage()
                .setLoginFieldText(generatingString(3, 14))
                .setPasswordFieldValidText()
                .clickLoginButtonForInvalidCredetials();

        String loginField = "//label['.mdc-text-field--filled']";
        String color1 = loginPage.driver.findElement(By.xpath(loginField)).getCssValue("background-color");
        System.out.println("rgba(245, 245, 245, 1)".equals(color1));
        Assertions.assertTrue(!"rgba(245, 245, 245, 1)".equals(color1) || "rgba(248, 227, 221, 0)".equals(color1)); //здесь с альфой не понятно, пикет не показывает на макете альфу.

        String errorField = "//div[contains(@class,\"error-block\")]";
        Assertions.assertFalse(loginPage.driver.findElement(By.xpath(errorField)).isDisplayed());
    }

    @Deprecated
    @Test
    @Feature("Тестирование поля логин. Негативная. Проверка на пустое поле")
    public void loginFieldNegativeWithEmptyLoginTest() {
        LoginPage loginPage = mainPage.gotoLoginPage()
                .setLoginFieldText("")
                .setPasswordFieldText("lkjsdfgjkg")
                .clickLoginButtonForInvalidCredetials();

        String loginField = "//label['.mdc-text-field--filled']";
        String color1 = loginPage.driver.findElement(By.xpath(loginField)).getCssValue("background-color");
        System.out.println("rgba(245, 245, 245, 1)".equals(color1));
        Assertions.assertTrue(!"rgba(245, 245, 245, 1)".equals(color1) || "rgba(248, 227, 221, 0)".equals(color1)); //здесь с альфой не понятно, пикет не показывает на макете альфу.

        String errorField = "//div[contains(@class,\"error-block\")]";
        Assertions.assertFalse(loginPage.driver.findElement(By.xpath(errorField)).isDisplayed());
    }

    @Test
    @Feature("Тестирование поля логин. Позитивная. Проверка граничных значений длины поля 3 и 20")
    public void loginFieldNegativeWithLimitValueCheckTest() {
        BlogPage blogPage = mainPage.gotoLoginPage()
                .setLoginFieldText("qaz")
                .setPasswordFieldText("4eae18cf9e")
                .clickLoginButtonForValidCredetials();
        System.out.println(blogPage.driver.getPageSource());
        Assertions.assertTrue(blogPage.driver.getPageSource().contains("Blog"));

        blogPage.logout();

        blogPage = mainPage.gotoLoginPage()
                .setLoginFieldText("qazwsxedcrfvtgbyhnuj")
                .setPasswordFieldText("a259276c97")
                .clickLoginButtonForValidCredetials();
        System.out.println(blogPage.driver.getPageSource());
        Assertions.assertTrue(blogPage.driver.getPageSource().contains("Blog"));
    }

    @Deprecated
    @Test
    @Feature("Тестирование поля логин. Негативная. Проверка пограничных значений длины поля 2, 4, 19, 21")
    public void loginFieldNegativeWithNearLimitValueCheckTest() {
        BlogPage blogPage = mainPage.gotoLoginPage()
                .setLoginFieldText("qazw")
                .setPasswordFieldText("cd5fd79e4f")
                .clickLoginButtonForValidCredetials();
        System.out.println(blogPage.driver.getPageSource());
        Assertions.assertTrue(blogPage.driver.getPageSource().contains("Blog"));

        blogPage.logout();

        blogPage = mainPage.gotoLoginPage()
                .setLoginFieldText("qazwsxedcrfvtgbyhnu")
                .setPasswordFieldText("5084a6164c")
                .clickLoginButtonForValidCredetials();
        System.out.println(blogPage.driver.getPageSource());
        Assertions.assertTrue(blogPage.driver.getPageSource().contains("Blog"));

        blogPage.logout();

        LoginPage loginPage = mainPage.gotoLoginPage()
                .setLoginFieldText("qa")
                .setPasswordFieldText("lkjsdf23455wegsiofzd756872gjkg")
                .clickLoginButtonForInvalidCredetials();

        String loginField = "//label['.mdc-text-field--filled']";
        String color1 = loginPage.driver.findElement(By.xpath(loginField)).getCssValue("background-color");
        System.out.println("rgba(245, 245, 245, 1)".equals(color1));
        Assertions.assertTrue(!"rgba(245, 245, 245, 1)".equals(color1) || "rgba(248, 227, 221, 0)".equals(color1)); //здесь с альфой не понятно, пикет не показывает на макете альфу.

        String errorField = "//div[contains(@class,\"error-block\")]";
        Assertions.assertFalse(loginPage.driver.findElement(By.xpath(errorField)).isDisplayed());

        loginPage = mainPage.gotoLoginPage()
                .setLoginFieldText("qazwsxedcrfvtgbyhnujm")
                .setPasswordFieldText("lkjsdf23455wegsiofzd756872gjkg")
                .clickLoginButtonForInvalidCredetials();
        Assertions.assertTrue(!"rgba(245, 245, 245, 1)".equals(color1) || "rgba(248, 227, 221, 0)".equals(color1)); //здесь с альфой не понятно, пикет не показывает на макете альфу.
        Assertions.assertFalse(loginPage.driver.findElement(By.xpath(errorField)).isDisplayed());
    }

    //Field Password
    @Deprecated
    @Test
    @Feature("Тестирование поля пароль. Негативная. Проверка на пустое поле")
    public void passwordFieldNegativeWithEmptyLoginTest() {
        LoginPage loginPage = mainPage.gotoLoginPage()
                .setLoginFieldValidText()
                .setPasswordFieldText("")
                .clickLoginButtonForInvalidCredetials();

        String loginField = "//label['.mdc-text-field--filled']";
        String color1 = loginPage.driver.findElement(By.xpath(loginField)).getCssValue("background-color");
        System.out.println("rgba(245, 245, 245, 1)".equals(color1));
        Assertions.assertTrue(!"rgba(245, 245, 245, 1)".equals(color1) || "rgba(248, 227, 221, 0)".equals(color1)); //здесь с альфой не понятно, пикет не показывает на макете альфу.

        String errorField = "//div[contains(@class,\"error-block\")]";
        Assertions.assertFalse(loginPage.driver.findElement(By.xpath(errorField)).isDisplayed());
    }

    @Deprecated
    @Test
    @Feature("Тестирование поля логин. Негативная. Проверка JS-скриптом")
    public void passwordFieldNegativeWithScriptTest() {
        LoginPage loginPage = mainPage.gotoLoginPage()
                .setLoginFieldValidText()
                .setPasswordFieldText("\"<script>alert(document.cookie)</script>, <!DOCTYPE html> <html> <head> <title>HTML Meta Tag</title> <meta http-equiv = \"\"refresh\"\" content = \"\"3; url = https://habr.com/ru/all/\"\" /> <body> <p>2</p> <head> </body> </html>\n" +
                        "<!DOCTYPE html><html><head><meta http-equiv = \"\"refresh\"\" content = \"\"3; url = https://habr.com/ru/all/\"\" /><head></html>\"")
                .clickLoginButtonForInvalidCredetials();

        String loginField = "//label['.mdc-text-field--filled']";
        String color1 = loginPage.driver.findElement(By.xpath(loginField)).getCssValue("background-color");
        System.out.println("rgba(245, 245, 245, 1)".equals(color1));
        Assertions.assertTrue("rgba(245, 245, 245, 1)".equals(color1) || !"rgba(248, 227, 221, 0)".equals(color1)); //здесь с альфой не понятно, пикет не показывает на макете альфу.

        String errorField = "//div[contains(@class,\"error-block\")]";
        Assertions.assertTrue(loginPage.driver.findElement(By.xpath(errorField)).isDisplayed());
    }

    @Deprecated
    @Test
    @Feature("Тестирование поля логин. Негативная. Проверка SQL-инъекцией")
    public void passwordFieldNegativeWithSQLTest() {
        LoginPage loginPage = mainPage.gotoLoginPage()
                .setLoginFieldValidText()
                .setPasswordFieldText("'UNION SELECT * from users LIMIT 10; --") //здесь надо знать название таблицы с юзерами
                .clickLoginButtonForInvalidCredetials();

        String loginField = "//label['.mdc-text-field--filled']";
        String color1 = loginPage.driver.findElement(By.xpath(loginField)).getCssValue("background-color");
        System.out.println("rgba(245, 245, 245, 1)".equals(color1));
        Assertions.assertTrue("rgba(245, 245, 245, 1)".equals(color1) || !"rgba(248, 227, 221, 0)".equals(color1)); //здесь с альфой не понятно, пикет не показывает на макете альфу.

        String errorField = "//div[contains(@class,\"error-block\")]";
        Assertions.assertTrue(loginPage.driver.findElement(By.xpath(errorField)).isDisplayed());
    }

    @Deprecated
    @Test
    @Feature("Тестирование поля логин. Негативная. Проверка строкой > 64К")
    public void passwordFieldNegativeWithBigStringTest() {
        LoginPage loginPage = mainPage.gotoLoginPage()
                .setLoginFieldValidText()
                .setPasswordFieldText(bigString) //здесь надо знать название таблицы с юзерами
                .clickLoginButtonForInvalidCredetials();

        String loginField = "//label['.mdc-text-field--filled']";
        String color1 = loginPage.driver.findElement(By.xpath(loginField)).getCssValue("background-color");
        System.out.println("rgba(245, 245, 245, 1)".equals(color1));
        Assertions.assertTrue("rgba(245, 245, 245, 1)".equals(color1) || !"rgba(248, 227, 221, 0)".equals(color1)); //здесь с альфой не понятно, пикет не показывает на макете альфу.

        String errorField = "//div[contains(@class,\"error-block\")]";
        Assertions.assertTrue(loginPage.driver.findElement(By.xpath(errorField)).isDisplayed());
    }

    @AfterEach
    void tearDown() {
        LogEntries logs = driver.manage().logs().get(LogType.BROWSER);
        for (LogEntry log : logs) {
            Allure.addAttachment("Browser stacktrace:\n", log.getMessage());
        }
        ((JUnitExtention) testWatcher)
                .setScreenShot(new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        driver.quit();
    }

    private String generatingString(int type, int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        int[] leftMargin = new int[]{65, 97, 48, 32, 58, 91, 123, 0, 128};
        int[] rightMargin = new int[]{90, 122, 57, 47, 64, 96, 127, 31, 255};
        switch (type) {
            case 0: { //строка с латинскими буквами
                char[] simbol = new char[2];
                for (int i = 0; i < length; i++) {
                    simbol[0] = (char) (new Random().nextInt(rightMargin[0] - leftMargin[0]) + leftMargin[0]);
                    simbol[1] = (char) (new Random().nextInt(rightMargin[1] - leftMargin[1]) + leftMargin[1]);
                    stringBuilder.append(simbol[new Random().nextInt(1)]);
                }
                break;
            }
            case 1: { //строка с латинскими буквами и цифрами
                char[] simbol = new char[3];
                for (int i = 0; i < length; i++) {
                    simbol[0] = (char) (new Random().nextInt(rightMargin[0] - leftMargin[0]) + leftMargin[0]);
                    simbol[1] = (char) (new Random().nextInt(rightMargin[1] - leftMargin[1]) + leftMargin[1]);
                    simbol[2] = (char) (new Random().nextInt(rightMargin[2] - leftMargin[2]) + leftMargin[2]);
                    stringBuilder.append(simbol[new Random().nextInt(2)]);
                }
                break;
            }
            case 2: { //строка с латинскими буквами, цифрами и спецсимволами
                char[] simbol = new char[9];
                for (int i = 0; i < length; i++) {
                    simbol[0] = (char) (new Random().nextInt(rightMargin[0] - leftMargin[0]) + leftMargin[0]);
                    simbol[1] = (char) (new Random().nextInt(rightMargin[1] - leftMargin[1]) + leftMargin[1]);
                    simbol[2] = (char) (new Random().nextInt(rightMargin[2] - leftMargin[2]) + leftMargin[2]);
                    simbol[3] = (char) (new Random().nextInt(rightMargin[3] - leftMargin[3]) + leftMargin[3]);
                    simbol[4] = (char) (new Random().nextInt(rightMargin[4] - leftMargin[4]) + leftMargin[4]);
                    simbol[5] = (char) (new Random().nextInt(rightMargin[5] - leftMargin[5]) + leftMargin[5]);
                    simbol[6] = (char) (new Random().nextInt(rightMargin[6] - leftMargin[6]) + leftMargin[6]);
                    simbol[7] = (char) (new Random().nextInt(rightMargin[7] - leftMargin[7]) + leftMargin[7]);
                    simbol[8] = (char) (new Random().nextInt(rightMargin[8] - leftMargin[8]) + leftMargin[8]);
                    stringBuilder.append(simbol[new Random().nextInt(8)]);
                }
                break;
            }
            case 3: { //строка с русскими буквами
                String[] string = new String[] {
                        "а", "б", "в", "г", "д", "е", "ё", "ж", "з", "и", "й"
                        , "к", "л", "м", "н", "о", "п", "р", "с", "т", "у", "ф", "х", "ц"
                        , "ч", "ш", "щ", "ъ", "ы", "ь", "э", "ю", "я"
                        , "А", "Б", "В", "Г", "Д", "Е", "Ё", "Ж", "З", "И", "Й"
                        , "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ц"
                        , "Ч", "Ш", "Щ", "Ъ", "Ы", "Ь", "Э", "Ю", "Я"
                };
                for (int i = 0; i < length; i++) {
                    stringBuilder.append(string[new Random().nextInt(33)]);
                }

                break;
            }
        }

        return stringBuilder.toString();
    }
}
