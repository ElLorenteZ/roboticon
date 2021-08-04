package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.Robot;
import io.lorentez.roboticon.model.RobotTeam;
import io.lorentez.roboticon.model.RobotTeamStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RobotRepository extends CrudRepository<Robot, Long> {

    List<Robot> findAll();

    @Query(value = "SELECT DISTINCT rt FROM RobotTeam rt " +
            "LEFT JOIN FETCH rt.team t " +
            "LEFT JOIN FETCH rt.robot r " +
            "WHERE r.id = :robotId AND rt.timeRemoved IS NULL AND rt.status = :status")
    Optional<RobotTeam> getRobotByActualStatus(Long robotId, RobotTeamStatus status);

    @Query(value = "SELECT DISTINCT rt FROM RobotTeam rt " +
            "LEFT JOIN FETCH rt.team t " +
            "LEFT JOIN FETCH rt.robot r " +
            "WHERE r.id = :robotId AND rt.timeRemoved IS NULL")
    List<RobotTeam> getRobotActualStatuses(Long robotId);

    @Query(value = "SELECT DISTINCT r FROM Robot r " +
            "LEFT JOIN FETCH r.robotTeams rt " +
            "LEFT JOIN FETCH rt.team t")
    List<Robot> findAllWithTeam();
}

