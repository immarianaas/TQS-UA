package ua.tqs.ex2;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.IOException;


import static org.assertj.core.api.Assertions.assertThat;

// @ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource( locations = "/application-integrationtest.properties")
public class IntegrationTestWithMysql {
    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CarRepository carRep;

    @AfterEach
    public void resetDB() {
        carRep.deleteAll();
    }

    @Test
    public void whenValidInput_thenCreateCar() {
        Car c1 = new Car("maker1", "model1");
        ResponseEntity<Car> entity = restTemplate.postForEntity("/api/cars", c1, Car.class);

        List<Car> found = carRep.findAll();
        assertThat(found).extracting(Car::getMaker).containsOnly("maker1");
    }


    @Test
    public void givenEmployees_whenGetEmployees_thenStatus200()  {
        createTestCar("maker1", "model1");
        createTestCar("maker2", "model2");


        ResponseEntity<List<Car>> response = restTemplate
                .exchange("/api/cars", HttpMethod.GET, null, new ParameterizedTypeReference<List<Car>>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(Car::getMaker).containsExactly("maker1", "maker2");

    }

    private void createTestCar(String maker, String model) {
        Car c = new Car(maker, model);
        carRep.saveAndFlush(c);
    }

}
