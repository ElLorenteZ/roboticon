package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.RobotTeam;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RobotTeamRepository extends CrudRepository<RobotTeam, Long> {

    @Query(value = "SELECT COUNT(rt) > 0 FROM RobotTeam rt WHERE rt.robot.id = :robotId AND rt.team.id = :teamId")
    boolean isRobotOwnedByTeam(Long robotId, Long teamId);

}
