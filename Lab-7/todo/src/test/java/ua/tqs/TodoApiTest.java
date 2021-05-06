package ua.tqs;

import static io.restassured.RestAssured.*;
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
        // assumindo que é o 4o elemento e nao o elemento c id 4
        // o elemento com id=4
        get("https://jsonplaceholder.typicode.com/todos")
        .then()
        .statusCode(200)
        .assertThat()
        .body("[3].title", equalTo("et porro tempora"));
        */

        given()
        .when()
            .get("https://jsonplaceholder.typicode.com/todos/4")
        .then().assertThat()
            .statusCode(200)
            .and().body("title", equalTo("et porro tempora"))
            .and().body("id", equalTo(4));

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
