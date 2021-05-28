package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.model.University;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {

    List<Team> findByUniversityNull();
    List<Team> findByUniversity(University university);

}
