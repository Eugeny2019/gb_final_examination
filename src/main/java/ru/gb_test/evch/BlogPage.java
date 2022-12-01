package ru.gb_test.evch;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class BlogPage extends BaseView{

    public BlogPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//li[@class='svelte-1rc85o5 mdc-menu-surface--anchor']")
    private WebElement dropdownButton;

    @FindBy(xpath = "//span[contains(@class,'mdc-deprecated-list-item__text') and contains(., 'Logout')] ")
    private WebElement logout;

    @FindBy(xpath = "//button[contains(@class, 'button mdc-button mdc-button--raised mdc-ripple-upgraded')]")
    private WebElement loginButton;

    public LoginPage logout() {
        webDriverWait.until(ExpectedConditions.visibilityOf(dropdownButton));
        dropdownButton.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(logout));
        logout.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(loginButton));
        return new LoginPage(driver);
    }
}
