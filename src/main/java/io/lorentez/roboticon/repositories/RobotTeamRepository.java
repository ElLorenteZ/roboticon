package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.RobotTeam;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RobotTeamRepository extends CrudRepository<RobotTeam, Long> {

}
