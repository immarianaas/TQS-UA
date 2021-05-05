package ua.tqs;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

public class TodoApiTest {
    
    @Test
    public void givenUrl_checkIfEndpointIsAvailable() {
        // check if endpoint is available (stat code 200)
        get("https://jsonplaceholder.typicode.com/todos")
            .then()
            .assertThat()
            .statusCode(200);
    }

    @Test
    public void testNumber4onTodoList() {
        // the title of ToDo #4 is “et porro tempora”
        /*
        // estou a assumir que é o 4o elemento (index 3), e não
        // o elemento com id=4
        get("https://jsonplaceholder.typicode.com/todos")
        .then()
        .statusCode(200)
        .assertThat()
        .body("[3].title", equalTo("et porro tempora"));
        */
        /*
        get("https://jsonplaceholder.typicode.com/todos")
        .then()
        .statusCode(200)
        .assertThat()
        .body("$", hasItem(
            allOf(
                hasEntry("id", 4)
                hasEntry("title", "et porro tempora")
            )
            ));
            */
            get("https://jsonplaceholder.typicode.com/todos")
            .then()
            .statusCode(200)
            .assertThat()
            .body("id", contains(4) /*, hasEntry("title", "et porro tempora") */
                );
            //.body(arguments, matcher, additionalKeyMatcherPairs);
    }

    @Test
    public void testTodoContains198and199() {
        get("https://jsonplaceholder.typicode.com/todos")
        .then()
        .statusCode(200)
        .assertThat()
        .body("id", hasItems(198, 199));
    }
}
