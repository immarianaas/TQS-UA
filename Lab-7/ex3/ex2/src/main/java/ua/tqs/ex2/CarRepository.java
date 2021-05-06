package ua.tqs.ex2;

import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CarRepository extends JpaRepository<Car,Long>  {
    // public Optional<Car> findById(Long id);
    public List<Car> findAll();
}
