package ru.gb_test.evch;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.ByteArrayInputStream;

public class JUnitExtention implements TestWatcher {
    ByteArrayInputStream screenShot;

    public void setScreenShot(ByteArrayInputStream screenShot) {
        this.screenShot = screenShot;
    }

    public void testFailed(ExtensionContext context, Throwable cause) {
        Allure.addAttachment("Скриншот перед закрытие браузера", screenShot);
    }
}
