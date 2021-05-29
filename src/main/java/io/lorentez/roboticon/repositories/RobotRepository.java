package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.Robot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RobotRepository extends CrudRepository<Robot, Long> {
}
