package com.saucedemo.pageObjects;

import com.saucedemo.ModernBasePage;
import com.saucedemo.text.LoremIpsum;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import org.openqa.selenium.By;

import javax.xml.crypto.Data;

import static com.saucedemo.helpers.ElementsInteraction.*;
import static com.saucedemo.pageObjects.GlobalSteps.getDynamicFieldValue;
import static com.saucedemo.pageObjects.GlobalSteps.getDynamicLocationFieldValue;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class CheckoutYourInformationPage extends ModernBasePage {
    private static final By FIRST_NAME_INPUT = By.id("first-name");
    private static final By LAST_NAME_INPUT = By.id("last-name");
    private static final By ZIP_INPUT = By.id("postal-code");
    private static final By CONTINUE_BUTTON = By.xpath("//input[@value='CONTINUE']");
    private static final By HEADER_FIELD = By.className("subheader");

    public CheckoutYourInformationPage() {
    }

    public CheckoutYourInformationPage(boolean takeScreenShot) {
        super(takeScreenShot);
    }

    public void isPageOpened() {
        getWait().until(titleIs("Swag Labs"));
        assertElementText(HEADER_FIELD, "Checkout: Your Information");
    }

    @And("^I am on Checkout your information page$")
    public void verifyIsPageOpened() {
        waitForOpen();
    }

    @And("^I set '(.*)' as first name on Checkout your information page$")
    public void setFirstName(String firstName) {
        sendKeys(FIRST_NAME_INPUT, getDynamicFieldValue(firstName, LoremIpsum.getInstance().getFirstName()));
    }

    @And("^I set '(.*)' as last name on Checkout your information page$")
    public void setLastName(String lastName) {
        sendKeys(LAST_NAME_INPUT, getDynamicFieldValue(lastName, LoremIpsum.getInstance().getLastName()));
    }

    @And("^I set '(.*)' as zip on Checkout your information page$")
    public void setZip(String zip) {
        sendKeys(ZIP_INPUT, getDynamicLocationFieldValue(zip));
    }

    @And("^I click 'Continue' button on Checkout your information page$")
    public CheckoutOverviewPage clickContinue() {
        click(CONTINUE_BUTTON);
        return new CheckoutOverviewPage(true);
    }
}
