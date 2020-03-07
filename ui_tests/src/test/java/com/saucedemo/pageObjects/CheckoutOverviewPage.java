package com.saucedemo.pageObjects;

import com.saucedemo.ModernBasePage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import org.openqa.selenium.By;

import static com.saucedemo.helpers.ElementsInteraction.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class CheckoutOverviewPage extends ModernBasePage {
    private static final By FINISH_BUTTON = By.linkText("FINISH");
    private static final By HEADER_FIELD = By.className("subheader");

    public CheckoutOverviewPage() {
    }

    public CheckoutOverviewPage(boolean takeScreenShot) {
        super(takeScreenShot);
    }

    public void isPageOpened() {
        getWait().until(titleIs("Swag Labs"));
        assertElementText(HEADER_FIELD, "Checkout: Overview");
    }

    @And("^I am on Checkout overview page$")
    public void verifyIsPageOpened() {
        waitForOpen();
    }

    @And("^The following products are available on Checkout overview page:$")
    public void verifyProductsInCart(DataTable dataTable) {
        System.out.println("lol");
    }

    @And("^I click 'Finish' button on Checkout overview page$")
    public CheckoutCompletePage clickFinish() {
        click(FINISH_BUTTON);
        return new CheckoutCompletePage(true);
    }
}
