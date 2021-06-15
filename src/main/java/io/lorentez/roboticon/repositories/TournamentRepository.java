package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.Tournament;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentRepository extends CrudRepository<Tournament, Long> {

    @Query(value = "SELECT DISTINCT t FROM Tournament t LEFT JOIN t.competitions c LEFT JOIN c.registrations r ORDER BY t.dateStart ASC")
    List<Tournament> findAllWithCompetitions();

}
