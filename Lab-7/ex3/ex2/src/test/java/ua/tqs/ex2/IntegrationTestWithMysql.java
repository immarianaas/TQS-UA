package ua.tqs.ex2;


import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.restassured.RestAssured;

import org.json.simple.JSONObject;

// @ExtendWith(SpringExtension.class)
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class IntegrationTestWithMysql {
    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CarRepository carRep;

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer()
                    .withUsername("demo")
                    .withPassword("passwd")
                    .withDatabaseName("tqs1");


    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    Car car1, car2;

    @BeforeEach
    public void setup() {
                car1 = carRep.saveAndFlush(new Car("maker11", "model11"));
                car2 = carRep.saveAndFlush(new Car("maker22", "model22"));
    }

    @AfterEach
    public void resetDB() {
        // carRep.deleteAll();
    }


    @Test
    public void postCarTest() {
        String url = "http://localhost:"+ port +"/api/cars/";
        Car new_car = new Car("new_maker", "new_model");

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("maker", new_car.getMaker());
        jsonObj.put("model", new_car.getModel());

        RestAssured
            .given()
                .contentType("application/json")
                .body(jsonObj.toString())
            .when()
                .post(url)
            .then()
                .assertThat()
                .statusCode(201);


        RestAssured
            .when().get(url)
            .then()
                .assertThat()
                .statusCode(200)
                .body("model", Matchers.hasItem(new_car.getModel()));
    }
// ----

    @Test
    public void testGetCar() {
        String url = "http://localhost:"+ port +"/api/cars/"+ car1.getId();

        RestAssured
            .when().get(url)
            .then()
                .assertThat()
                .statusCode(202)
                .body("model", Matchers.equalTo(car1.getModel()));
    }




}
