package com.saucedemo.helpers;

import com.saucedemo.http.HttpRequestFactory;
import com.saucedemo.selenium.SeleniumConfig;
import com.saucedemo.selenium.WaitForAjaxCalls;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.saucedemo.CucumberHooks.getDriver;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Select element from active search results.
 */
public final class ElementsInteraction {
    private static final Logger LOG = LoggerFactory.getLogger(ElementsInteraction.class);
    private static final int TIMEOUT = SeleniumConfig.getConfig().getPageLoadingRetriesCount();

    private ElementsInteraction() {
    }

    public static WebDriverWait getWait() {
        return new WebDriverWait(getDriver(), TIMEOUT);
    }

    public static void click(By elementLocator) {
        getWait().until(elementToBeClickable(elementLocator)).click();
    }

    public static void click(WebElement element) {
        getWait().until(elementToBeClickable(element)).click();
    }

    public static void click(By elementLocator, int xOffset, int yOffset) {
        new Actions(getDriver())
                .moveToElement(getDriver().findElement(elementLocator), xOffset, yOffset)
                .click()
                .build()
                .perform();
    }

    public static void markCheckbox(WebElement checkbox, String state) {
        List<String> states = new ArrayList<>(Arrays.asList("checked", "unchecked"));
        assertTrue(states.contains(state.toLowerCase()), state + " should be in " + states.toString());
        boolean expectedSelectedState = state.equalsIgnoreCase("checked");
        if (expectedSelectedState != checkbox.isSelected()) {
            new Actions(getDriver()).moveToElement(checkbox, 5, 5).click().perform();
        }
        getWait().until(elementSelectionStateToBe(checkbox, expectedSelectedState));
    }

    public static void markCheckbox(By checkboxElementLocator, String state) {
        markCheckbox(getWait().until(presenceOfElementLocated(checkboxElementLocator)), state);
    }

    public static void clear(WebElement element) {
        element.clear();
        if (!getText(element).isEmpty()) {
            int characters = getText(element).length();
            while (characters > 0) {
                element.sendKeys(Keys.BACK_SPACE);
                characters--;
            }
        }
        assertTrue(getText(element) == null || Objects.requireNonNull(getText(element)).isEmpty(),
                String.format("Field should be empty: '%s'", getText(element)));
    }

    public static void clear(By inputElementLocator) {
        verifyElementPresence(inputElementLocator);
        clear(getDriver().findElement(inputElementLocator));
    }

    public static void sendKeys(By inputElementLocator, String text) {
        sendKeys(inputElementLocator, text, false);
    }

    public static void sendKeys(By inputElementLocator, String text, boolean isAutocomplete) {
        click(inputElementLocator);
        WebElement input = getDriver().findElement(inputElementLocator);
        if (isAutocomplete) {
            char[] chars = text.toCharArray();
            for (char character : chars) {
                input.sendKeys(String.valueOf(character));
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new WaitForAjaxCalls(getDriver()).checkPendingRequests();
            }
            assertEquals(text, getText(input), "Text is typed properly");
            new WaitForAjaxCalls(getDriver()).checkPendingRequests();
            input.sendKeys(Keys.ENTER);
        } else {
            if (SeleniumConfig.getConfig().getSeleniumBrowserIsGrid()) {
                String fileUrl = String.join("/", SeleniumConfig.getConfig().getSeleniumGridUrl(),
                        "clipboard", ((RemoteWebDriver) getDriver()).getSessionId().toString());
                new HttpRequestFactory().sendRequest(fileUrl, "POST", text, null);
            } else {
                StringSelection fieldValueSelection = new StringSelection(text);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(fieldValueSelection, fieldValueSelection);
            }
            getDriver().findElement(inputElementLocator).sendKeys(Keys.SHIFT, Keys.INSERT);
            new WaitForAjaxCalls(getDriver()).checkPendingRequests();
//        Then check if it equals to the expected value
            assertEquals(getText(input), text, "Text is typed properly");
        }
    }

    /**
     * Used when Collapse menu element is a button.
     *
     * @param menuElementLocator    - Main element
     * @param subMenuElementLocator - Element from submenu
     */
    public static void clickOnMenuOption(By menuElementLocator, By subMenuElementLocator) {
        WebDriver driver = getDriver();
//        Hover menu element action
        Actions hoverMenuAction = new Actions(driver);
//        Move mouse to the menu element
        hoverMenuAction.moveToElement(driver.findElement(menuElementLocator));
//        Perform action
        hoverMenuAction.build().perform();
//        Wait for menu to be opened
        getWait().until(presenceOfElementLocated(subMenuElementLocator));
//        Select menu option action
        Actions selectMenuOptionAction = new Actions(driver);
//        Click on menu option
        selectMenuOptionAction.click(driver.findElement(subMenuElementLocator));
//        Perform action
        selectMenuOptionAction.build().perform();
//        Select menu option action
        Actions unHoverMenuOptionAction = new Actions(driver);
//        Move cursor away from menu
        unHoverMenuOptionAction.moveByOffset(250, 250);
//        Perform action
        unHoverMenuOptionAction.build().perform();
    }

    public static void selectByVisibleText(By selectElementLocator, String optionText) {
        verifyElementPresence(selectElementLocator);
        WebElement selectWebElement = getDriver().findElement(selectElementLocator);
        Select select = new Select(selectWebElement);
        select.selectByVisibleText(optionText);
        assertEquals(optionText, getText(select.getFirstSelectedOption()), "Selected option text");
    }

    public static String getText(WebElement element) {
        String text = element.getText();
        if (!text.isEmpty()) {
            return element.getText();
        } else if (element.getAttribute("value") != null) {
            return element.getAttribute("value");
        } else if (element.getAttribute("innerText") != null) {
            return element.getAttribute("innerText");
        } else {
            return text;
        }
    }

    public static String getText(By elementLocator) {
        return getText(getWait().until(visibilityOfElementLocated(elementLocator)));
    }

    /**
     * highlight with red border element located by locator for one second
     *
     * @param elementLocator element locator
     */
    private static void highlightElement(By elementLocator) {
        highlightElement(getDriver().findElement(elementLocator));
    }

    /**
     * highlight with red border specified web element for one second
     *
     * @param element web element
     */
    private static void highlightElement(WebElement element) {
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].style.border='3px solid red'", element);
    }

    public static boolean getAvailability(String availability) {
        assertTrue(availability.matches("(available|not available)"),
                availability + "should be 'available' or 'not available'");
        return availability.equals("available");
    }

    public static void verifyElementPresence(By elementLocator, String availability) {
        verifyElementPresence(elementLocator, getAvailability(availability));
    }

    public static void verifyElementPresence(By elementLocator) {
        verifyElementPresence(elementLocator, true);
    }

    public static void verifyElementPresence(By elementLocator, boolean availability) {
        if (availability) {
            getWait().until(visibilityOfElementLocated(elementLocator));
        } else {
            waitForElementAbsence(elementLocator);
        }
    }

    public static void waitForElementAbsence(By elementLocator) {
        try {
            getWait().until(numberOfElementsToBe(elementLocator, 0));
        } catch (WebDriverException e) {
            highlightElement(elementLocator);
            throw e;
        }
    }

    public static void assertElementText(By elementLocator, String text) {
        getWait().ignoring(StaleElementReferenceException.class)
                .until(textToBe(elementLocator, text));
    }

    public static void assertElementValue(By elementLocator, String text) {
        getWait().ignoring(StaleElementReferenceException.class)
                .until(textToBePresentInElementValue(elementLocator, text));
    }

    public static int getColumnIndex(String columnName) {
        By columnNameCell = By.xpath("//thead//td");
        getWait().ignoring(StaleElementReferenceException.class).until(visibilityOfAllElementsLocatedBy(columnNameCell));
        String lowerCaseColumnName = columnName.toLowerCase();
        List<String> cells = getDriver().findElements(columnNameCell).stream()
                .map(ElementsInteraction::getText)
                .map(cellText -> Objects.requireNonNull(cellText).trim().toLowerCase())
                .collect(Collectors.toList());
        assertTrue(cells.size() > 0, "Column headers should be available on the page");
        assertTrue(cells.contains(lowerCaseColumnName),
                String.format("%s column should be in [%s]", lowerCaseColumnName, String.join(", ", cells)));
//        xpath indexes (in the spots this index will be used) are starting from 1, not 0
        return cells.indexOf(lowerCaseColumnName) + 1;
    }

    public static By getLocator(String elementLocatorTemplate, String... dynamicLocatorParts) {
        return By.xpath(String.format(elementLocatorTemplate, dynamicLocatorParts));
    }
}
