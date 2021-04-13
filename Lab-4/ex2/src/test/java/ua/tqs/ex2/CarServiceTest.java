package ua.tqs.ex2;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;


/* test CarService (and mock the CarRepository) */

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {
    
    @Mock(lenient=true)
    private CarRepository carRep;

    @InjectMocks
    private CarManagerService carManServ;

    Car c1;
    Car c2;
    Car c3;
    

    @BeforeEach
    public void setUp() {
        c1 = new Car("maker1", "model1"); c1.setCarId(1L);
        c2 = new Car("maker2", "model2"); c2.setCarId(2L);
        c3 = new Car("maker3", "model3"); c3.setCarId(3L);

        List<Car> allCars = Arrays.asList(c1, c2, c3);
        Mockito.when(carRep.findByCarId(c1.getCarId())).thenReturn(c1);
        Mockito.when(carRep.findByCarId(c2.getCarId())).thenReturn(c2);
        Mockito.when(carRep.findByCarId(999L)).thenReturn(null);
        Mockito.when(carRep.findAll()).thenReturn(allCars);
    }

    @Test
    public void whenValidCarId_thenCarShouldBeFound() {
        Long id = 1L;
        Optional<Car> c = carManServ.getCarDetails(id);
        assertThat(c.isPresent()).isTrue();
        Car found = c.get();
        assertThat(found.getCarId()).isEqualTo(id);
        Mockito.verify(carRep, VerificationModeFactory.times(1)).findByCarId(id);
    }


    @Test
    public void whenInValidCarId_thenCarShouldNotBeFound() {
        Optional<Car> oc = carManServ.getCarDetails(999L);
        Mockito.verify(carRep, VerificationModeFactory.times(1)).findByCarId(999L);
        assertThat(oc).isNull();
    }

    @Test
    public void given3Cars_whengetAll_thenReturn3Records() {
        List<Car> allCars = carManServ.getAllCars();
        Mockito.verify(carRep, VerificationModeFactory.times(1)).findAll();
        assertThat(allCars).hasSize(3).extracting(Car::getModel).contains(c1.getModel(), c2.getModel(), c3.getModel());
    }

    
}
