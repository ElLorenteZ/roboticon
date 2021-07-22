package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.University;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UniversityRepository extends CrudRepository<University, Long> {

    Optional<University> findByNameContainingIgnoreCase(String name);
    List<University> findAllByNameContainingIgnoreCase(String name);
    List<University> findByCityIgnoreCase(String city);
    List<University> findByCountryIgnoreCase(String country);
    List<University> findAll();

}
