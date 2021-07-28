package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.Registration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends CrudRepository<Registration, Long> {

    @Query(value = "SELECT DISTINCT r " +
            "FROM Registration r " +
            "LEFT JOIN FETCH r.competition c " +
            "LEFT JOIN FETCH c.tournament t " +
            "LEFT JOIN FETCH r.robot rob " +
            "LEFT JOIN FETCH rob.robotTeams rt " +
            "LEFT JOIN FETCH rt.team team " +
            "LEFT JOIN FETCH r.users u " +
            "LEFT JOIN FETCH r.statuses st " +
            "WHERE t.id = :tournamentId AND team.id = :teamId")
    List<Registration> getTeamsRegistrationsInTournament(Long tournamentId, Long teamId);

    @Query(value = "SELECT DISTINCT r " +
            "FROM Registration r " +
            "LEFT JOIN FETCH r.competition c " +
            "LEFT JOIN FETCH c.tournament t " +
            "LEFT JOIN FETCH r.robot rob " +
            "LEFT JOIN FETCH rob.robotTeams rt " +
            "LEFT JOIN FETCH rt.team team " +
            "LEFT JOIN FETCH r.users u " +
            "LEFT JOIN FETCH r.statuses st " +
            "WHERE c.id = :competitionId")
    List<Registration> getRegistrationsForCompetition(Long competitionId);

    @Query(value = "SELECT DISTINCT r FROM Registration r " +
            "LEFT JOIN FETCH r.robot rob " +
            "LEFT JOIN FETCH r.competition c " +
            "LEFT JOIN FETCH r.users u " +
            "LEFT JOIN FETCH r.statuses s " +
            "WHERE r.id = :registrationId")
    Optional<Registration> findFetchAllInfoById(Long registrationId);

    @Query(value = "SELECT COUNT(r) > 0 FROM Registration r WHERE r.robot.id = :robotId AND r.competition.id = :competitionId")
    boolean isRobotRegistered(Long robotId, Long competitionId);
}
