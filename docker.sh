docker-compose down
docker-compose run tests mvn clean install -Dselenium.browser.isGrid=true -Dcucumber.filter.tags="@adminSignIn" -DparallelTestsCount=7
docker-compose down
