package ru.gb_test.evch;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoginPage extends BaseView {
    static Properties prop = new Properties();
    private static String login;
    private static String password;
    private static InputStream configFile;

    public LoginPage(WebDriver driver) {
        super(driver);
        try {
            configFile = new FileInputStream("src/main/resources/my.properties");
            prop.load(configFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        login = prop.getProperty("login");
        password = prop.getProperty("password");

    }

//    @FindBy(id = "email")
//    private WebElement emailField;
//
//    @FindBy(id = "passwd")
//    private WebElement passwordField;
//
//    @FindBy(id = "SubmitLogin")
//    private WebElement submitButton;
//
//    @Step("Login")
//    public void setEmailField(String login) {
//        emailField.sendKeys(login);
//    }

//    public void setPasswordField(String password) {
//        passwordField.sendKeys(password);
//    }
//
//    public void clickSubmitButton() {
//        submitButton.click();
//    }

//    public MyAccountPage login(String login, String password) {
//        setEmailField(login);
//        setPasswordField(password);
//        clickSubmitButton();
//        return new MyAccountPage(driver);
//    }

    @FindBy(xpath = "//input[contains(@class, 'mdc-text-field__input') and contains(@type, 'text')]")
    private WebElement loginField;

    public LoginPage setLoginFieldText(String login) {
        loginField.sendKeys(login);
        return this;
    }

    public LoginPage setLoginFieldValidText() {
        return setLoginFieldText(this.login);
    }

    @FindBy(xpath = "//input[contains(@class, 'mdc-text-field__input') and contains(@type, 'password')]")
    private WebElement passwordField;

    public LoginPage setPasswordFieldText(String password) {
        passwordField.sendKeys(password);
        return this;
    }

    public LoginPage setPasswordFieldValidText() {
        return setPasswordFieldText(this.password);
    }

    @FindBy(xpath = "//button[contains(@class, 'button mdc-button mdc-button--raised mdc-ripple-upgraded')]")
    private WebElement loginButton;

    private String studentPage = "//h1[contains(@class, 'svelte-') and contains(.,'Blog')]";

    public BlogPage clickLoginButtonForValidCredetials() {
        loginButton.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(studentPage)));
        return new BlogPage(driver);
    }

    public LoginPage clickLoginButtonForInvalidCredetials() {
        loginButton.click();

        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return this;
    }
}
