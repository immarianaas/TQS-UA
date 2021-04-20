Feature: Reserve a flight

    Scenario: Select departure and destination
        Given that I am on the website "https://blazedemo.com"
        When I select "Boston" as depature
            And I select "Berlin" as destination
            And I click the button
        Then "Flights from Boston to Berlin:" should appear on the page

    Scenario: Choose one flight
        Given that I am on the website "https://blazedemo.com"
            And I select "Boston" as departure
            And I select "Berlin" as destination
            And I click the button
        When I select the option number 2
        Then "Your flight from TLV to SFO has been reserved." should appear on the page

    Scenario: Purchase flight
        Given that I am on the website "https://blazedemo.com"
            And I select "Boston" as departure
            And I select "Berlin" as destination
            And I click the button
            And I select the option number 1
        When I type the name "Mariana" on the name input
            And I click on the purchase button
        Then "Thank you for your purchase today!" should appear on the page
            And "Status" should appear on the page
            And status should be equal to "PendingCapture"
            And page title should be equal to "BlazeDemo Confirmation"