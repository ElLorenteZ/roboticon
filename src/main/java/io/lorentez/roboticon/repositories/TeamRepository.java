package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.model.University;
import io.lorentez.roboticon.model.UserTeam;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
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

    @Query(value = "SELECT DISTINCT t FROM Team t LEFT JOIN FETCH t.robotTeams WHERE t.id = :id")
    Optional<Team> findByIdWithRobotTeams(Long id);

    @Query(value = "SELECT DISTINCT t FROM Team t LEFT JOIN FETCH t.robotTeams ORDER BY t.name")
    Set<Team> findAllByNameWithRobotTeams();

    @Query(value = "SELECT DISTINCT t FROM Team t LEFT JOIN FETCH t.userTeams u WHERE u.timeRemoved IS NULL AND u.user.id =:userId")
    List<Team> findUserTeams(Long userId);

    @Query(value = "SELECT DISTINCT ut FROM UserTeam ut LEFT JOIN FETCH ut.user u LEFT JOIN FETCH ut.team t WHERE t.id = :teamId AND u.email = :email AND ut.timeRemoved IS NULL ")
    Optional<UserTeam> findActualMembersByTeamId(Long teamId, String email);

    @Query(value = "SELECT DISTINCT t FROM Team t LEFT JOIN FETCH t.university LEFT JOIN FETCH t.userTeams ut LEFT JOIN FETCH t.robotTeams rt WHERE t.id = :id")
    Optional<Team> fetchSingleTeamInformation(Long id);
}
