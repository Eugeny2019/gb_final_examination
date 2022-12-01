package ru.gb_test.evch;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseView {
    public WebDriver driver;
    public WebDriverWait webDriverWait;
    public Actions actions;

    public BaseView(WebDriver driver) {
        this.driver = driver;
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(15), Duration.ofSeconds(3));
        actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }
}
