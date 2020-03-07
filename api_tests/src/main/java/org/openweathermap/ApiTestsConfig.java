package org.openweathermap;

import com.ydanylchenko.configuration.reader.Config;
import com.ydanylchenko.configuration.reader.properties.PropertiesLoader;
import com.ydanylchenko.configuration.reader.properties.Property;
import com.ydanylchenko.configuration.reader.properties.PropertyFile;

@PropertyFile("config.properties")
public class ApiTestsConfig extends Config {

    private static ApiTestsConfig config;

    public static ApiTestsConfig getConfig() {
        if (config == null) {
            config = new ApiTestsConfig();
        }
        return config;
    }

    public ApiTestsConfig() {
        PropertiesLoader.populate(this);
    }

    @Property("api.url")
    private String apiUrl;

    /**
     * @return API server URL
     */
    public String getApiUrl() {
        return apiUrl;
    }

    @Property("api.version")
    private String apiVersion;

    /**
     * @return Version of API
     */
    public String getApiVersion() {
        return apiVersion;
    }

    @Property("api.key")
    private String apiKey;

    /**
     * @return Authentication API Key
     */
    public String getApiKey() {
        return apiKey;
    }
}
