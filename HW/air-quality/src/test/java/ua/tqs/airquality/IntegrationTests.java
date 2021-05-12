package ua.tqs.airquality;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;


@WebMvcTest(ApiController.class)
class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataAccess dataAcc;

    @MockBean
    private Cache cache;

    @MockBean
    private CallExterior ext;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void givenCountry_thenReturnStations() {
        String country = "andorra";
        Mockito
                .when(dataAcc.getLocationsByCountry( anyString() ))
                .thenReturn(new HashMap<>() {{
                    put("Escaldes Engordany, Andorra", "andorra/fixa");
                }});

        RestAssuredMockMvc
                .given()
                .when().get("/api/stations?country="+country)
                .then()
                    .log().all()
                    .statusCode(200)
                    .body("country", Matchers.equalTo(country))
                    .body("stations.size()", is(1))
                    .body("stations[0].name", Matchers.equalTo("Escaldes Engordany, Andorra"))
                    .body("stations[0].uri", Matchers.equalTo("andorra/fixa"))
                    ;
    }

    @Test
    void givenInvalidCountry_thenReturnEmptyList() {
        String country = "invalid-country";
        Mockito
                .when(dataAcc.getLocationsByCountry( country ))
                .thenReturn(new TreeMap<String, String>());

        RestAssuredMockMvc
                .given()
                .when().get("/api/stations?country="+country)
                .then()
                .log().all()
                .statusCode(200)
                .body("stations.size()", is(0))
                ;

    }

    @Test
    void givenStation_thenReturnForecast() {
        String stationuri = "andorra/fixa";

        configureBehaviourInfoAndorra();

        RestAssuredMockMvc
                .given()
                .when().get("/api/forecast/?station="+stationuri)
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", is(3))
                .body("stationname", Matchers.equalTo("Escaldes Engordany, Andorra"))
                .body("stationuri", Matchers.equalTo(stationuri))
                .body("forecast[0].date", Matchers.equalTo("2021-04-29"))
                .body("forecast[0].values[0].avg", is(31))
                ;
    }

    @Test
    void givenStationAndType_thenReturnForecast() {
        String stationuri = "andorra/fixa";
        String t = "uvi";

        configureBehaviourInfoAndorra();

        RestAssuredMockMvc
                .given()
                .when().get("/api/forecast/?station="+stationuri+ "&type=" + t)
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", is(3))
                .body("stationname", Matchers.equalTo("Escaldes Engordany, Andorra"))
                .body("stationuri", Matchers.equalTo(stationuri))
                .body("forecast[0].date", Matchers.equalTo("2021-04-29"))
                .body("forecast[0].values[0].type", equalTo(t))
                .body("forecast[0].values[0].avg", is(0))
                ;
    }

    @Test
    void givenStationAndBadType_thenReturnHttpStatus400() {
        String stationuri = "andorra/fixa";
        String t = "bad-type";
        configureBehaviourInfoAndorra();
        RestAssuredMockMvc
                .given()
                .when().get("/api/forecast/?station="+stationuri+ "&type=" + t)
                .then()
                .log().all()
                .statusCode(400);
    }


    @Test
    void givenInvalidStation_thenReturnHttpCode404() {
        String station = "invalid-station";
        Mockito
                .when(dataAcc.getInfoByStation( station ))
                .thenReturn(null);

        RestAssuredMockMvc
                .given()
                .when().get("/api/forecast/?station="+station)
                .then()
                .log().all()
                .statusCode(404);
    }


    @Test
    void test_getCacheStats() {
        Mockito
                .when(cache.getNoHits())
                .thenReturn(1);
        Mockito
                .when(cache.getNoMisses())
                .thenReturn(2);
        Mockito
                .when(cache.getNoRequests())
                .thenReturn(3);

        RestAssuredMockMvc
                .given()
                .when().get("/api/cache")
                .then()
                .log().all()
                .statusCode(200)
                .body("hits", is(1))
                .body("misses", is(2))
                .body("requests", is(3))
                ;
    }

    /* --- helper --- */

    private void configureBehaviourInfoAndorra() {
        String stationuri = "andorra/fixa";
        TreeMap<String, HashMap<String, Integer[]>> data = new TreeMap<String, HashMap<String, Integer[]>>();
        data.put("2021-04-29", new HashMap<String, Integer[]>() {{
            put("o3", new Integer[] {31, 28, 33});
            put("pm10", new Integer[] {4, 4, 6});
            put("pm25", new Integer[] {14, 12, 23});
            put("uvi", new Integer[] {0, 0, 3});
        }});

        Mockito
                .when(dataAcc.getInfoByStation( stationuri ))
                .thenReturn( data );

        Mockito
                .when(dataAcc.getNameByUrl( stationuri ))
                .thenReturn("Escaldes Engordany, Andorra");
    }


}
