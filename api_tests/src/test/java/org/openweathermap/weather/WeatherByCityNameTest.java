package org.openweathermap.weather;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.openweathermap.ApiTestsConfig;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

/**
 * Create one test to get weather using Open Weather API in the city of London: http://openweathermap.org/current
 */
@Test
public class WeatherByCityNameTest {
    private RequestSpecification requestSpecification;

    @BeforeClass
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(ApiTestsConfig.getConfig().getApiUrl())
                .setBasePath(ApiTestsConfig.getConfig().getApiVersion())
                .addQueryParam("APPID", ApiTestsConfig.getConfig().getApiKey())
                .build();
    }

    @DataProvider(name = "cityData", parallel = true)
    public static Object[][] citiesData() {
        return new Object[][]{
                {"London", "GB", 2643743, -0.13f, 51.51f, 0}
        };
    }

    @Test(dataProvider = "cityData")
    public void getWeatherInLondonTest(String city, String country, Integer id, Float lon, Float lat, Integer timezone) {
        given()
                .spec(requestSpecification)
                .when()
                .param("q", city)
                .get("weather")
                .then()
                .statusCode(200)
                .assertThat().body(matchesJsonSchemaInClasspath("weather-schema.json"))
                .body("coord.lon", equalTo(lon))
                .body("coord.lat", equalTo(lat))
                .body("sys.country", equalTo(country))
                .body("timezone", equalTo(timezone))
                .body("id", equalTo(id))
                .body("name", equalTo(city));
    }
}
