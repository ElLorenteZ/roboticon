package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.model.University;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeamRepositoryTest {

    @Autowired
    UniversityRepository universityRepository;

    @Autowired
    TeamRepository teamRepository;

    @Test
    void findByUniversityNull() {
        //given

        //when
        List<Team> teams = teamRepository.findByUniversityNull();

        //
        assertNotNull(teams);
        assertThat(teams).isNotEmpty();
    }

    @Test
    void findByUniversity() {
        //given
        Optional<University> university = universityRepository
                .findByNameContainingIgnoreCase("Akademia Górniczo-Hutnicza");
        University aghUniversity = university.get();

        //when
        List<Team> teams = teamRepository.findByUniversity(aghUniversity);

        //then
        assertNotNull(teams);
        assertThat(teams).isNotEmpty();

    }
}