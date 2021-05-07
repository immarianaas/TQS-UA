package ua.tqs.airquality;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;


@WebMvcTest(ApiController.class)
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTests {

    /*
    @LocalServerPort
    private int port;
     */

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataAccess dataAcc;

    @BeforeEach
    public void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    public void givenCountry_thenReturnStations() {
        String country = "andorra";
        Mockito
                .when(dataAcc.getLocationsByCountry( anyString() ))
                .thenReturn(new HashMap<String, String>() {{
                    put("Escaldes Engordany, Andorra", "andorra/fixa");
                }});

        RestAssuredMockMvc
                .given()
                .when().get("/api/stations?country"+country)
                .then()
                    .log().all()
                    .statusCode(200)
                    .body("$.size()", Matchers.equalTo(1))
                    ;
    }

}
