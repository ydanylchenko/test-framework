package com.saucedemo.pageObjects;

import com.saucedemo.context.Platform;
import com.saucedemo.text.LoremIpsum;
import io.cucumber.java.en.Given;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.saucedemo.CucumberHooks.getContext;
import static com.saucedemo.CucumberHooks.getDriver;
import static com.saucedemo.TestsConfig.getConfig;
import static com.saucedemo.text.LoremIpsum.GENERATED;
import static org.testng.Assert.*;

public class GlobalSteps {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalSteps.class);

    @Given("^I open start page$")
    public LoginPage openRecruitersHomePage() {
        getContext().setCurrentPlatform(Platform.MAIN);
        getDriver().get(getConfig().getBaseUrl());
        return new LoginPage(true);
    }

    /**
     * @param value           - value that might be generated
     * @param generatedValues - default value if the value is not in context
     * @return generatedValues or the value from context
     */
    public static String getDynamicFieldValue(String value, String... generatedValues) {
//        Replace the generated parts that are already in context
        String valueWithoutGeneratedPartsFromContext = getDynamicFieldValue(value);
//        Amount of generated fields should not be less then the generated parts. With this we can be sure that all
//      the generated parts will be replaced
        long generatedPartsAmount = Arrays.stream(valueWithoutGeneratedPartsFromContext.split(" "))
                .filter(part -> part.startsWith(GENERATED))
                .count();
        assertTrue(generatedValues.length >= generatedPartsAmount,
                String.format("All generated parts in '%s' should have matching generated value: %s",
                        valueWithoutGeneratedPartsFromContext, String.join(",", generatedValues)));
//        Go through each part of the provided value and map the generated parts with generated values in context.
        ArrayDeque<String> generatedParts = new ArrayDeque<>(Arrays.asList(generatedValues));
        Arrays.stream(valueWithoutGeneratedPartsFromContext.split(" "))
                .filter(part -> part.startsWith(GENERATED))
                .forEach(generatedPart -> addGeneratedFieldToUserData(generatedPart, generatedParts.pop()));
//        As all the generated values are in context we can now replace them
        return getDynamicFieldValue(valueWithoutGeneratedPartsFromContext);
    }

    /**
     * @param value - value that might be partially generated
     * @return the value itself if it's not generated or the value with replaced generated parts
     */
    public static String getDynamicFieldValue(String value) {
        return value == null ? "" : getDynamicFieldValueAsObject(value).toString();
    }

    public static Object getDynamicFieldValueAsObject(String value) {
        String partsMap = value.replaceAll("\\w+", "%s");
        Map<String, String> generatedUserData = getContext().getDataStore();
        Object[] parts = Arrays.stream(value.split("(\\W)"))
                .map(part -> generatedUserData.getOrDefault(part, part))
                .filter(key -> !key.isEmpty())
                .toArray();
        return String.format(partsMap, parts);
    }

    public static String getFormattedItems(List<WebElement> rows) {
        List<String> formattedRows = rows.stream()
                .map(row -> (Objects.requireNonNull(row.getAttribute("innerText"))
                        .replaceAll("\t\n", "\t")
                        .replaceAll("\n", "\t")
                        .trim()))
                .collect(Collectors.toList());
        return String.join("\n", formattedRows);
    }

    public static void addGeneratedFieldToUserData(String fieldName, String fieldValue) {
        Map<String, String> dataStore = getContext().getDataStore();
        LOG.info("{}: '{}' added to context data store", fieldName, fieldValue);
        if (dataStore.containsKey(fieldName)) {
            LOG.warn(String.format("%s: %s in context is getting overridden", fieldName, dataStore.get(fieldName)));
        }
        dataStore.put(fieldName, fieldValue);
        assertTrue(dataStore.containsKey(fieldName), fieldName + " should be in context");
        assertEquals(dataStore.get(fieldName), fieldValue);
    }

    public static String getDynamicLocationFieldValue(String key) {
        key = getDynamicFieldValue(key);
        if (!key.startsWith(GENERATED)) {
            return key;
        } else {
            if (!getContext().getDataStore().containsKey(key)) {
                String part = key.split("(\\W)")[0];
                String city = "city";
                String state = "state";
                String fullState = "fullState";
                String zip = "zip";
                String generatedKey;
                if (part.endsWith(city)) {
                    generatedKey = part.substring(0, part.length() - city.length());
                } else if (part.endsWith(state)) {
                    generatedKey = part.substring(0, part.length() - state.length());
                } else if (part.endsWith(zip)) {
                    generatedKey = part.substring(0, part.length() - zip.length());
                } else {
                    generatedKey = part;
                }
                Map<String, String> location = LoremIpsum.getInstance().getLocation();
                addGeneratedFieldToUserData(generatedKey + city, location.get("city"));
                addGeneratedFieldToUserData(generatedKey + state, location.get("state"));
                addGeneratedFieldToUserData(generatedKey + zip, location.get("zip"));
                addGeneratedFieldToUserData(generatedKey + fullState, location.get("fullState"));
            }
            return getDynamicFieldValue(key);
        }
    }
}
