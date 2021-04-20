package ua.tqs;


import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CalculatorSteps {
    private Calculator calc;

    @Given("a calculator I just turned on")
    public void a_calculator_i_just_turned_on() {
        calc = new Calculator();
    }

    @When("I add {int} and {int}")
    public void i_add_and(Integer int1, Integer int2) {
        calc.push(int1);
        calc.push(int2);
        calc.push("+");
    }

    @When("I subtract {int} to {int}")
    public void i_subtract_to(Integer int1, Integer int2) {
        calc.push(int1);
        calc.push(int2);
        calc.push("-");
    }

    
    @Then("the result is {int}")
    public void the_result_is(Integer int1) {
        assertEquals(int1, calc.value().intValue());
    }
}
