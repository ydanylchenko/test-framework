package com.saucedemo.tests;

import com.saucedemo.CucumberHooks;
import com.saucedemo.pageObjects.*;
import com.saucedemo.text.LoremIpsum;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PurchaseTest {

    @BeforeTest
    public void setUp() throws MalformedURLException, InterruptedException {
        new CucumberHooks().beforeTest();
    }

    @AfterTest
    public void tearDown() throws IOException {
        new CucumberHooks().afterTest(null);
    }

    /**
     * The good version of this test is available in purchaseAllTShirts.feature
     */
    @Test
    public void purchaseAllTShirts() {
        LoginPage loginPage = new GlobalSteps().openStartPage();
        loginPage.setEmail("standard_user");
        loginPage.setPassword("secret_sauce");
        ProductsPage productsPage = loginPage.clickSignInButton();
        productsPage.clickAddToCart("Sauce Labs Bolt T-Shirt");
        Header header = new Header(true);
        header.verifyCartSize("1");
        productsPage.clickAddToCart("Test.allTheThings() T-Shirt (Red)");
        header.verifyCartSize("2");
        CartPage cartPage = header.clickCart();
        Map<String, String> sauceLabsBoltTShirt = new HashMap<>();
        {
            sauceLabsBoltTShirt.put("quantity", "1");
            sauceLabsBoltTShirt.put("product", "Sauce Labs Bolt T-Shirt");
            sauceLabsBoltTShirt.put("description", "Get your testing superhero on with the Sauce Labs bolt T-shirt. From American Apparel, 100% ringspun combed cotton, heather gray with red bolt.");
            sauceLabsBoltTShirt.put("price", "15.99");
        }
        Map<String, String> testAllTheThingsTShirtRed = new HashMap<>();
        {
            testAllTheThingsTShirtRed.put("quantity", "1");
            testAllTheThingsTShirtRed.put("product", "Test.allTheThings() T-Shirt (Red)");
            testAllTheThingsTShirtRed.put("description", "This classic Sauce Labs t-shirt is perfect to wear when cozying up to your keyboard to automate a few tests. Super-soft and comfy ringspun combed cotton.");
            testAllTheThingsTShirtRed.put("price", "15.99");
        }
        cartPage.verifyCart("available", Arrays.asList(sauceLabsBoltTShirt, testAllTheThingsTShirtRed));
        CheckoutYourInformationPage checkoutYourInformationPage = cartPage.clickCheckout();
        checkoutYourInformationPage.setFirstName(LoremIpsum.getInstance().getFirstName());
        checkoutYourInformationPage.setLastName(LoremIpsum.getInstance().getLastName());
        checkoutYourInformationPage.setZip(LoremIpsum.getInstance().getLocation().get("zip"));
        CheckoutOverviewPage checkoutOverviewPage = checkoutYourInformationPage.clickContinue();
        checkoutOverviewPage.verifyCart("available", Arrays.asList(sauceLabsBoltTShirt, testAllTheThingsTShirtRed));
        CheckoutCompletePage checkoutCompletePage = checkoutOverviewPage.clickFinish();
    }
}
