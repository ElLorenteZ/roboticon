package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.Robot;
import io.lorentez.roboticon.model.RobotTeam;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RobotRepository extends CrudRepository<Robot, Long> {

    @Query(value = "SELECT DISTINCT rt FROM RobotTeam rt LEFT JOIN FETCH rt.team t LEFT JOIN FETCH rt.robot r WHERE r.id = :robotId")
    Optional<RobotTeam> getRobotActualOwnership(Long robotId);

}
