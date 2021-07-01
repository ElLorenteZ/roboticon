package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.model.University;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {

    List<Team> findAllByUniversityNull();
    List<Team> findAllByUniversity(University university);
    Optional<Team> findByNameContainingIgnoreCase(String name);

    @Query(value = "SELECT DISTINCT t FROM Team t LEFT JOIN FETCH t.robotTeams WHERE LOWER(t.name) LIKE %:name%")
    Optional<Team> findByNameWithRobotTeams(String name);

    @Query(value = "SELECT DISTINCT t FROM Team t LEFT JOIN FETCH t.robotTeams ORDER BY t.name")
    Set<Team> findAllByNameWithRobotTeams();

    @Query(value = "SELECT DISTINCT t FROM Team t LEFT JOIN FETCH t.userTeams u WHERE u.timeRemoved IS NULL AND u.user.email =:email")
    List<Team> findUserTeams(String email);

}
