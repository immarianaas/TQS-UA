package ua.tqs.ex2;

import org.hibernate.engine.spi.EntityUniqueKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@DataJpaTest
public class CarRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository carRep;

    @Test
    public void whenFindc1ByCarId_thenReturnc1Car() {
        Car c1 = new Car("maker1", "model1");
        entityManager.persistAndFlush(c1);

        Car found = carRep.findByCarId(c1.getCarId());
        assertThat(found).isEqualTo(c1);
    }

    @Test
    public void whenFindByInvalidId_thenReturnNull() {
        Car res = carRep.findByCarId(-99L);
        assertThat(res).isNull();
    }

    @Test
    public void givenSetOfCars_whenFindAll_thenReturnAllCars() {
        Car c1 = new Car("maker1", "model1"); 
        Car c2 = new Car("maker2", "model2"); 
        Car c3 = new Car("maker3", "model3"); 

        entityManager.persist(c1);
        entityManager.persist(c2);
        entityManager.persist(c3);
        entityManager.flush();

        List<Car> allCars = carRep.findAll();
        assertThat(allCars).hasSize(3).extracting(Car::getModel).containsOnly(c1.getModel(), c2.getModel(), c3.getModel());
    }
}
