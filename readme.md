### Intro
This is selenium based UI test automation framework for both member and recruiter sides.

### Prerequisites

1. [Java](https://en.wikipedia.org/wiki/Java_(programming_language)) - is used as the main coding language. 
1. [Maven](https://maven.apache.org/) - is used as build and dependencies management tool. 

#### Optional

1. [Docker](https://www.docker.com/) - is used for run of Selenoid on CI.
1. [Selenoid](https://aerokube.com/selenoid/) - is used as default containerized browsers provider on Jenkins (CI).

### Libraries

1. [JUnit (4)](https://junit.org/junit4/) - is used for tests run and assertions.
1. [Cucumber](https://docs.cucumber.io/) - with junit connector is used to add behaviour driven development (BDD) support.
1. [Selenium](https://www.seleniumhq.org/) - is used as cross-platform browser interaction API.

### Test Cases Structure

1. Tests are written in BDD (behaviour driven development) format that is implemented using 
[Cucumber library](https://github.com/cucumber/cucumber). The idea is that you are splitting the test case steps from 
their implementation, as result test cases are written in resources folder 
(/ladders_ui_tests/src/test/resources/com/theladders) in files with "*.feature" extension formatted in 
[gherkin syntax](https://docs.cucumber.io/gherkin/reference/). Each of the steps annotated with the step tag (Given, 
When, Then, And or But) is mapped to one step implementation (i.e. Java method) by regex. The steps implementation is 
divided by classes by following [PageObject](https://github.com/SeleniumHQ/selenium/wiki/PageObjects) pattern that 
stands for representation of each web page (or it's part) as separate class and the idea that each of the methods 
is a service that page provides to the end user. In aaddition to that you have the control of user flow that defines 
where you can get from which page with verification that the expected page was opened. 
1. As the tests are UI based the Selenium library is used for browser interaction. The browser factory is implemented 
with thread local approach that allows you to have one browser opened for each of the test run thread. There are two ways of browser 
startup that are supported by the [web browser factory](/ladders_ui_tests/src/main/java/com/theladders/selenium/WebDriverFactory.java): 
    1. [using locally installed browser](https://www.seleniumhq.org/docs/03_webdriver.jsp#introducing-the-selenium-webdriver-api-by-example) - 
    in this case on browser creation the browser that is installed on your workstation is run. This is preferred way 
    during development as you can interact with browser directly.
    1. [using selenium grid](https://www.seleniumhq.org/docs/07_selenium_grid.jsp) - where you connect to a hub that has 
    variety of browsers connected to it. Browsers in this case can be deployed on different workstations and hub is 
    providing you access (note that just the access but not the orchestration) to the cluster where you can specify 
    which browser you would like to use by [desired capabilities functionality](https://github.com/SeleniumHQ/selenium/wiki/Grid2#using-grid-to-run-tests)
    This is preferred way for running tests on Jenkins and for cross platform multithreaded tests execution. Currently we are using 
    dockerized selenium grid called [selenoid](https://aerokube.com/selenoid/) but there are other options available 
    on market, e.g. [Sauce Labs](https://saucelabs.com/), [BrowserStack](https://www.browserstack.com/) etc.
1. [Run of the tests](/ladders_ui_tests/src/test/java/com/theladders/RunCucumberTests.java) as well 
as [various assertions](/ladders_ui_tests/src/test/java/com/theladders/helpers/ElementsInteraction.java) 
are done using JUnit library. Project is built using Maven tool on tests phase. On tests phase maven locates JUnit 
@RunWith annotation that is tied up with cucumber and it's [hooks](/ladders_ui_tests/src/test/java/com/theladders/CucumberHooks.java). 
After execution of hooks annotated with @Before annotation the tests execution is started with the specified 
[cucumber options](https://docs.cucumber.io/cucumber/api/#optio). Configuration can be updated on the fly by specifying 
the options as part of maven build command. The default ones are part of the [cucumber tests runner @CucumberOptions 
annotation](/ladders_ui_tests/src/test/java/com/theladders/RunCucumberTest.java) As soon as tests execution is done 
the [hooks](/ladders_ui_tests/src/test/java/com/theladders/CucumberHooks.java) annotated with @After tag is executed.

### Test Framework Configuration Options
All the test framework related configurations are stored in [config.properties](/ladders_ui_tests/src/test/resources/config.properties) file 
but each of the properties can be overwritten by the same named [environment variable](https://maven.apache.org/guides/introduction/introduction-to-profiles.html) 
during tests run with maven. E.g. _selenium.browser.isGrid=false_ is set to _false_ in [config.properties](/ladders_ui_tests/src/test/resources/config.properties)
but during run on _Jenkins_ we need to use selenium grid so that we are setting it to true by:

```
mvn clean install -Dselenium.browser.isGrid=true
``` 

### [Continuous Integration (Jenkins)](https://jenkins.aws.theladders.com/view/AutomationQA/)

#### Build jobs:

1. [AutomationQA](https://jenkins.aws.theladders.com/view/AutomationQA/) - executed all the tests:

    1. nightly
    1. on changes in QA-Release branch of Recruiter-App projects
    1. on build of JobSeeker-LW-Deploy-QA 

#### Nodes 

1. All the QA env based tests are executed on [qa-server-test](https://jenkins.aws.theladders.com/computer/qa-server-test/)
1. All the Production env based tests are executed on [qa-server-prodTest](https://jenkins.aws.theladders.com/computer/qa-server-prodTest/)

# Troubleshooting Notes

1. If browser doesn't start:
    1. Check if version of chromedriver (_chromedriver(.exe) -v_ in terminal) matches version of chrome browser
    1. Check if Glue is set to _com.theladders_ in Run/Debug Configurations > Templates > Cucumber java > Glue line 
1. You should be connected to VPN
