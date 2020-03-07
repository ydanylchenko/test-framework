package com.saucedemo.context;

import com.saucedemo.TestsConfig;

public enum Platform {
    MAIN("MAIN", TestsConfig.getConfig().getBaseUrl());
    private String name;
    private String baseUrl;

    Platform(String name, String baseUrl) {
        this.name = name;
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getName() {
        return name;
    }

    /**
     * returns current tab platform object by current page url
     *
     * @param currentUrl url of page that is currently opened
     * @return platform object
     */
    public static Platform getByUrl(String currentUrl) {
        Platform currentPlatform = null;
        Platform[] platforms = values();
        for (Platform platform : platforms) {
            if (currentUrl.startsWith(platform.getBaseUrl())) {
                currentPlatform = platform;
                break;
            } else {
                System.out.println(platform.getBaseUrl() + " not in\n" + currentUrl);
            }
        }
        return currentPlatform;
    }

    @Override
    public String toString() {
        return String.format("%s platform deployed at: %s", name, baseUrl);
    }
    }
