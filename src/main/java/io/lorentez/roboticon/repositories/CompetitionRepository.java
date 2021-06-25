package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.Competition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompetitionRepository extends CrudRepository<Competition, Long> {

    @Query("SELECT DISTINCT c FROM Competition c LEFT JOIN FETCH c.registrations WHERE c.id = :id")
    Optional<Competition> findWithRegistrationsById(Long id);

}
