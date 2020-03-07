package com.saucedemo.text;

import com.saucedemo.context.Platform;

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
    /**
     * Keyword for datatable that should be used as prefix to specify the dataStore key value that
     * is randomly taken from page
     */
    public static final String ANY = "_any_";
    /**
     * Keyword for datatable that should be used as prefix to specify the dataStore key value that is read from page
     */
    public static final String POSTED = "_posted_";

    /**
     * Keyword for datatable that should be used to specify the webelement that should not be available on page
     */
    public static final String MISSING = "_missing_";

    private Random random;
    private List<String> words;
    private List<String> maleNames;
    private List<String> femaleNames;
    private List<String> surnames;
    private List<String> firstNames;
    private List<String> countries;
    private List<String> locations;
    private List<String> phoneAreaCodes;
    private List<String> memberNumberOfEmployees;
    private List<String> recruiterNumberOfEmployees;
    private List<String> checkboxStates;
    private List<String> industries;
    private List<String> companies;
    private List<String> schools;
    private List<String> bonusMeasurements;
    private List<String> jobTitles;
    private List<String> skills;
    private List<String> recruitersYearsOfExperience;
    private List<String> adminYearsOfExperience;
    private List<String> membersYearsOfExperience;
    private List<String> searchYearsOfExperience;
    private List<String> otherBonuses;
    private List<String> bonuses;
    private List<String> recruitersSalaries;
    private List<String> membersSalaries;
    private List<String> adminSalaries;
    private List<String> roles;
    private List<String> adminRoles;
    private List<String> distances;
    private List<String> recruiterDegrees;
    private List<String> membersDegrees;
    private List<String> workAuthorizations;
    private List<String> securityClearances;
    private List<String> jobsiteUnassignReasons;
    private List<String> companyTypes;
    private List<String> companySizes;
    private List<String> level;
    private List<String> emailFrequencies;

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

    public LoremIpsum() {
        this(new Random());
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
        companies = readLines("companies.txt");
        jobTitles = readLines("job_titles.txt");
        skills = readLines("skills.txt");
        bonuses = readLines("recruiters/bonuses.txt");
        bonusMeasurements = readLines("recruiters/bonus_measurements.txt");
        industries = readLines("recruiters/industries.txt");
        phoneAreaCodes = readLines("phone_area_codes.txt");
        memberNumberOfEmployees = readLines("members/number_of_employees.txt");
        recruiterNumberOfEmployees = readLines("recruiters/number_of_employees.txt");
        otherBonuses = readLines("recruiters/other_bonuses.txt");
        recruitersSalaries = readLines("recruiters/salaries.txt");
        membersSalaries = readLines("members/salaries.txt");
        adminSalaries = readLines("admin/salaries.txt");
        workAuthorizations = readLines("members/work_authorizations.txt");
        securityClearances = readLines("members/security_clearances.txt");
        jobsiteUnassignReasons = readLines("admin/jobsite_unassign_reasons.txt");
        companyTypes = readLines("recruiters/companyTypes.txt");
        companySizes = readLines("recruiters/companySizes.txt");
        level = readLines("admin/levels.txt");
        roles = readLines("roles.txt");
        adminRoles = readLines("admin/roles.txt");
        schools = readLines("schools.txt");
        recruitersYearsOfExperience = readLines("recruiters/years_of_experience.txt");
        membersYearsOfExperience = readLines("members/years_of_experience.txt");
        adminYearsOfExperience = readLines("admin/years_of_experience.txt");
        searchYearsOfExperience = readLines("recruiters/search_years_of_experience.txt");
        distances = readLines("recruiters/distances.txt");
        recruiterDegrees = readLines("recruiters/degrees.txt");
        membersDegrees = readLines("members/degrees.txt");
        emailFrequencies = readLines("recruiters/email_frequencies.txt");
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

    /*
     * (non-Javadoc)
     *
     * @see com.saucedemo.text.Lorem#getCountry()
     */
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

    /*
     * (non-Javadoc)
     *
     * @see com.saucedemo.text.Lorem#getEmail()
     */
    public String getEmail() {
        return getEmail(false);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.saucedemo.text.Lorem#getEmail()
     */
    public String getEmail(boolean preApproved) {
        return String.format("ydanylchenko+%s@%s", getDateTime(), preApproved ? "saucedemo.com" : "gmail.com");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.saucedemo.text.Lorem#getFirstName()
     */
    public String getFirstName() {
        return getRandom(firstNames);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.saucedemo.text.Lorem#getFirstNameMale()
     */
    public String getFirstNameMale() {
        return getRandom(maleNames);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.saucedemo.text.Lorem#getFirstNameFemale()
     */
    public String getFirstNameFemale() {
        return getRandom(femaleNames);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.saucedemo.text.Lorem#getLastName()
     */
    public String getLastName() {
        return getRandom(surnames);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.saucedemo.text.Lorem#getName()
     */
    public String getName() {
        return getFirstName() + " " + getLastName();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.saucedemo.text.Lorem#getNameMale()
     */
    public String getNameMale() {
        return getFirstNameMale() + " " + getLastName();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.saucedemo.text.Lorem#getNameFemale()
     */
    public String getNameFemale() {
        return getFirstNameFemale() + " " + getLastName();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.saucedemo.text.Lorem#getTitle(int)
     */
    public String getTitle(int count) {
        return getWords(count, count, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.saucedemo.text.Lorem#getTitle(int, int)
     */
    public String getTitle(int min, int max) {
        return getWords(min, max, true);
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

    /*
     * (non-Javadoc)
     *
     * @see com.saucedemo.text.Lorem#getHtmlParagraphs(int, int)
     */
    public String getHtmlParagraphs(int min, int max) {
        int count = getCount(min, max);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append("<p>");
            sb.append(getParagraphs(1, 1));
            sb.append("</p>");
        }
        return sb.toString().trim();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.saucedemo.text.Lorem#getParagraphs(int, int)
     */
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

    /*
     * (non-Javadoc)
     *
     * @see com.saucedemo.text.Lorem#getUrl()
     */
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

    /*
     * (non-Javadoc)
     *
     * @see com.saucedemo.text.Lorem#getWords(int)
     */
    public String getWords(int count) {
        return getWords(count, count, false);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.saucedemo.text.Lorem#getWords(int, int)
     */
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

    /*
     * (non-Javadoc)
     *
     * @see com.saucedemo.text.Lorem#getPhoneExtension()
     */
    public String getPhoneExtension() {
        return String.format("%03d", random.nextInt(100));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.saucedemo.text.Lorem#getPassword()
     */
    public String getPassword() {
        return "P@ssw0rd";
    }

    public String getCheckboxState() {
        return getRandom(checkboxStates);
    }

    public String getIndustry() {
        return getRandom(industries);
    }

    public String getCompany() {
        return getRandom(companies);
    }

    public String getSchool() {
        return getRandom(schools);
    }

    public String getBonusMeasurement() {
        return getRandom(bonusMeasurements);
    }

    public String getOtherBonus() {
        return getRandom(otherBonuses);
    }

    public String getBonus() {
        return getRandom(bonuses);
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
