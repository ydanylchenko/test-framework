#Login as standard_user and purchase ALL the T-shirts.
Feature: Recruiter Sign Up

  Background:
    Given I open start page
    And I set 'standard_user' as username on Sign in page
    And I set 'secret_sauce' as password on Sign in page
    And I click 'Sign In' button on Sign in page

  @recruiters @recruiterSignUp @recruiterEmail
  Scenario Outline: Purchase <product> product
    When I click 'ADD TO CART' button on '<product>' product on Products page
    Then Cart contains '1' item on Header
    And I click on cart icon on Header
    And The following products are available on Cart page:
      | product   | description   | price   | quantity |
      | <product> | <description> | <price> | 1        |
    And I click 'Checkout' button on Cart page
    And I set '_generated_firstName' as first name on Checkout your information page
    And I set '_generated_lastName' as last name on Checkout your information page
    And I set '_generated_location_zip' as zip on Checkout your information page
    And I click 'Continue' button on Checkout your information page
    And The following products are available on Checkout overview page:
      | product   | description   | price   | quantity |
      | <product> | <description> | <price> | 1        |
    And I click 'Finish' button on Checkout overview page
    Then I am on Checkout complete page

    Scenarios:
      | product                           | description                                                                                                                            | price |
      | Sauce Labs Backpack               | carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection. | 29.99 |
#      | Sauce Labs Bike Light             |                                                                                                                                        |       |
#      | Sauce Labs Bolt T-Shirt           |                                                                                                                                        |       |
#      | Sauce Labs Fleece Jacket          |                                                                                                                                        |       |
#      | Sauce Labs Onesie                 |                                                                                                                                        |       |
#      | Test.allTheThings() T-Shirt (Red) |                                                                                                                                        |       |
