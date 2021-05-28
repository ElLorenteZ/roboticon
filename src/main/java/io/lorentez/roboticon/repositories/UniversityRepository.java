package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.University;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniversityRepository extends CrudRepository<University, Long> {

    List<University> findByNameContainingIgnoreCase(String name);
    List<University> findByCityIgnoreCase(String city);
    List<University> findByCountryIgnoreCase(String country);

}
