package com.saucedemo.pageObjects;

import com.saucedemo.ModernBasePage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;

import static com.saucedemo.helpers.ElementsInteraction.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

public class CartPage extends ModernBasePage {
    private static final By CHECKOUT_BUTTON = By.className("checkout_button");
    private static final By HEADER_FIELD = By.className("subheader");

    public CartPage() {
    }

    public CartPage(boolean takeScreenShot) {
        super(takeScreenShot);
    }

    public void isPageOpened() {
        getWait().until(titleIs("Swag Labs"));
        assertElementText(HEADER_FIELD, "Your Cart");
    }

    @Given("^I am on Recruiters start page$")
    public void verifyIsPageOpened() {
        waitForOpen();
    }

    @When("^The following products are available on Cart page:$")
    public void verifyProductsInCart(DataTable dataTable) {
        System.out.println("lol");
    }

    @When("^I click 'Checkout' button on Cart page$")
    public CheckoutYourInformationPage clickCheckout() {
        click(CHECKOUT_BUTTON);
        return new CheckoutYourInformationPage();
    }
}
