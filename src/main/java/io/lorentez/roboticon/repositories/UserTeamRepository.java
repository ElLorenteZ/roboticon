package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.UserTeam;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTeamRepository extends CrudRepository<UserTeam, Long> {
}
