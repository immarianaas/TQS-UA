package ua.tqs.ex2;

import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CarRepository extends JpaRepository<Car,Long>  {
    public Car findByCarId(Long id);
    public List<Car> findAll();
}
