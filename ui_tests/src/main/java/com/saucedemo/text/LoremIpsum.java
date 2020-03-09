package com.saucedemo.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class LoremIpsum {
    private static LoremIpsum instance;
    /**
     * Keyword for datatable that should be used as prefix to specify the dataStore key value that is generated with
     * this class
     */
    public static final String GENERATED = "_generated_";

    private Random random;
    private List<String> words;
    private List<String> maleNames;
    private List<String> femaleNames;
    private List<String> surnames;
    private List<String> firstNames;
    private List<String> countries;
    private List<String> locations;
    private List<String> phoneAreaCodes;
    private List<String> checkboxStates;

    private String[] URL_HOSTS = new String[]{
            "https://www.bing.com/search?q=%s",
            "https://search.yahoo.com/search?p=%s"};

    public static LoremIpsum getInstance() {
        if (instance == null) {
            synchronized (LoremIpsum.class) {
                if (instance == null) {
                    instance = new LoremIpsum(new Random());
                }
            }
        }
        return instance;
    }

    public LoremIpsum(Random random) {
        this.random = random;

        words = readLines("lorem.txt");
        maleNames = readLines("male_names.txt");
        femaleNames = readLines("female_names.txt");
        surnames = readLines("surnames.txt");
        firstNames = new ArrayList<>();
        firstNames.addAll(maleNames);
        firstNames.addAll(femaleNames);
        countries = readLines("countries.txt");
        locations = readLines("locations.txt");
        checkboxStates = readLines("checkbox_states.txt");
        phoneAreaCodes = readLines("phone_area_codes.txt");
    }

    private static String getDateTime() {
        return ZonedDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmssSSS"));
    }

    private List<String> readLines(String file) {
        List<String> ret = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(file), UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                ret.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    public Map<String, String> getLocation() {
        String[] mergedLocation = getRandom(locations).split(",");
        Map<String, String> locationMap = new HashMap<>();
        locationMap.put("zip", mergedLocation[0]);
        locationMap.put("state", mergedLocation[1]);
        locationMap.put("fullState", mergedLocation[2]);
        locationMap.put("city", mergedLocation[3]);
        return locationMap;
    }

    public String getCountry() {
        return getRandom(countries);
    }

    public String getStreetAddress() {
        String building = getDigit(1, 9);
        String streetName = getWords(2, true);
        String streetType = getRandom(Arrays.asList("Alley", "Avenue", "Boulevard", "Byway", "Close", "Crescent",
                "Court", "Drive", "Highway", "Lane", "Place", "Road", "Route", "Street", "Way"));
        return String.join(" ", building, streetName, streetType);
    }

    public String getEmail(boolean isInternal) {
        return String.format("ydanylchenko+%s@%s", getDateTime(), isInternal ? "saucedemo.com" : "gmail.com");
    }

    public String getFirstName() {
        return getRandom(firstNames);
    }

    public String getFirstNameMale() {
        return getRandom(maleNames);
    }

    public String getFirstNameFemale() {
        return getRandom(femaleNames);
    }

    public String getLastName() {
        return getRandom(surnames);
    }

    public String getName() {
        return getFirstName() + " " + getLastName();
    }

    private int getCount(int min, int max) {
        if (min < 0)
            min = 0;
        if (max < min)
            max = min;
        return max != min ? random.nextInt(max - min) + min : min;
    }

    public String getDigit(int min, int max) {
        return String.valueOf(getCount(min, max));
    }

    public String getParagraphs(int min, int max) {
        int count = getCount(min, max);
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < count; j++) {
            int sentences = random.nextInt(5) + 2; // 2 to 6
            for (int i = 0; i < sentences; i++) {
                sb.append(getWords(1, 1, true));
                sb.append(" ");
                sb.append(getWords(4, 5, false));
                sb.append(". ");
            }
            sb.append("\n");
        }
        return sb.toString().trim();
    }

    public String getUrl() {
        StringBuilder sb = new StringBuilder();
        int hostId = random.nextInt(URL_HOSTS.length);
        String host = String.format(URL_HOSTS[hostId], getWords(1));
        sb.append(host);
        return sb.toString();
    }

    private String getWords(int min, int max, boolean title) {
        int count = getCount(min, max);
        return getWords(count, title);
    }

    public String getWords(int count) {
        return getWords(count, count, false);
    }

    public String getWords(int min, int max) {
        return getWords(min, max, false);
    }

    private String getWords(int count, boolean title) {
        StringBuilder sb = new StringBuilder();
        int size = words.size();
        int wordCount = 0;
        while (wordCount < count) {
            String word = words.get(random.nextInt(size));
            if (title) {
                if (wordCount == 0 || word.length() > 3) {
                    word = word.substring(0, 1).toUpperCase()
                            + word.substring(1);
                }
            }
            sb.append(word);
            sb.append(" ");
            wordCount++;
        }
        return sb.toString().trim();
    }

    public String getRandom(List list) {
        int size = list.size();
        return String.valueOf(list.get(random.nextInt(size)));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.saucedemo.text.Lorem#getPhone()
     */
    public String getPhone() {
        return String.format("(%s) %d%02d-%04d",
                getRandom(phoneAreaCodes),
                random.nextInt(4) + 6,
                random.nextInt(100),
                random.nextInt(10000));
    }

    public String getCheckboxState() {
        return getRandom(checkboxStates);
    }

    public String getDateBetween(int yearFrom, int monthFrom, int dayFrom, int yearTo, int monthTo, int dayTo) {
        LocalDate dateFrom = LocalDate.of(yearFrom, monthFrom, dayFrom);
        LocalDate dateTo = LocalDate.of(yearTo, monthTo, dayTo);
        int year = Integer.parseInt(getDigit(dateFrom.getYear(), dateTo.getYear()));
        int day = Integer.parseInt(getDigit(dateFrom.getDayOfYear(), dateTo.getDayOfYear()));
        LocalDate date = LocalDate.ofYearDay(year, day);
        return date.toString();
    }
}
