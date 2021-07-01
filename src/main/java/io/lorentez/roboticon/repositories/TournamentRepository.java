package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.Tournament;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TournamentRepository extends CrudRepository<Tournament, Long> {

    @Query(value = "SELECT DISTINCT t FROM Tournament t LEFT JOIN FETCH t.competitions c LEFT JOIN FETCH c.registrations r ORDER BY t.dateStart ASC, c.name ASC")
    List<Tournament> findAllWithCompetitions();

    @Query(value = "SELECT DISTINCT t FROM Tournament t LEFT JOIN FETCH t.competitions c LEFT JOIN FETCH c.registrations r WHERE t.id IN (:ids) ORDER BY t.dateStart ASC")
    List<Tournament> searchForCompetition(Set<Long> ids);

    @Query(nativeQuery = true, value = "SELECT t.id FROM Tournament t LEFT JOIN Competition c ON c.tournament_id = t.id WHERE UPPER(t.name) LIKE UPPER(\'%\'||:keyword||\'%\') OR UPPER(c.name) LIKE UPPER(\'%\'||:keyword||\'%\')")
    Set<Long> getSearchedTournamentsIds(String keyword);


}

