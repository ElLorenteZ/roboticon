package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.CompetitionType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionTypeRepository extends CrudRepository<CompetitionType, Long> {
}
