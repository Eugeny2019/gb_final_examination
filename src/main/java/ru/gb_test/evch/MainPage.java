package ru.gb_test.evch;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MainPage extends BaseView {
    public MainPage(WebDriver driver) {
        super(driver);
    }

    //существует бага, по которой пользователь не разлогинивается. Как будет исправлено, так можно раскомментировать.
//    @FindBy(xpath = "//div[@class='mdc-menu mdc-menu-surface']")
//    private WebElement dropdownButton;

//    @FindBy(xpath = "//span[contains(@class,'mdc-deprecated-list-item__text') and contains(., 'Login')] ")
//    private WebElement login;

    @Step("Клик на Hello, autotest")
    public LoginPage gotoLoginPage() {
//        webDriverWait.until(ExpectedConditions.visibilityOf(dropdownButton));
//        dropdownButton.click();
//        webDriverWait.until(ExpectedConditions.visibilityOf(login));
//        login.click();
        return new LoginPage(driver);
    }
}
