package ua.tqs.ex2;

import static org.mockito.Mockito.when;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;

import net.minidev.json.JSONUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.springframework.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

// create a test to verify the Car[Rest]Controller (and mock the CarService bean).
@WebMvcTest(CarController.class)
public class CarControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CarManagerService carManagerService;

    @Test
    public void whenPostCar_thenCreateCar() throws Exception {
        Car newcar = new Car("maker1", "modelo1");
        when(carManagerService.save(Mockito.any())).thenReturn(newcar);

        System.err.println(newcar.toString());
        System.err.println(Arrays.toString(JsonUtil.toJson(newcar)));

        mvc.perform(
            post("/api/cars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.toJson(newcar))
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.maker", is("maker1")));

        verify(carManagerService, times(1)).save(Mockito.any());
    }    

    @Test
    public void givenCar_whenGetCars_thenReturnCarsArray() throws Exception {
        Car c1 = new Car("maker1", "model1");
        Car c2 = new Car("maker2", "model2");
        Car c3 = new Car("maker3", "model3");

        List<Car> allCars = Arrays.asList(c1, c2, c3);

        given(carManagerService.getAllCars()).willReturn(allCars);
        mvc.perform(
            get("/api/cars")
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[0].maker", is(c1.getMaker())))
        .andExpect(jsonPath("$[1].maker", is(c2.getMaker())));

    }
}
