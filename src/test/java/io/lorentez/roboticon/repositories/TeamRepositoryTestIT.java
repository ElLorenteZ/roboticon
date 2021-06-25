package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.Robot;
import io.lorentez.roboticon.model.RobotTeam;
import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.model.University;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeamRepositoryTestIT {

    private static final String SAMPLE_TEAM_NAME = "Sample Team";
    private static final String SAMPLE_TEAM_NAME2 = "Sample Team With Sample Robot";
    private static final String SAMPLE_ROBOT_NAME = "Sample Robot";

    @Autowired
    RobotRepository robotRepository;

    @Autowired
    UniversityRepository universityRepository;

    @Autowired
    TeamRepository teamRepository;



    @Test
    void findAllByUniversityNull() {
        //given

        //when
        List<Team> teams = teamRepository.findAllByUniversityNull();

        //
        assertNotNull(teams);
        assertThat(teams).isNotEmpty();
    }

    @Test
    void findAllByUniversity() {
        //given
        Optional<University> university = universityRepository
                .findByNameContainingIgnoreCase("Akademia Górniczo-Hutnicza");
        University aghUniversity = university.get();

        //when
        List<Team> teams = teamRepository.findAllByUniversity(aghUniversity);

        //then
        assertNotNull(teams);
        assertThat(teams).isNotEmpty();
    }

    @Test
    void findByNameContainingIgnoreCase(){
        //given
        String name = "sunrise";

        //when
        Optional<Team> sunriseOptional = teamRepository.findByNameContainingIgnoreCase(name);

        //then
        assertThat(sunriseOptional).isPresent();
        assertThat(sunriseOptional.get().getName()).containsIgnoringCase(name);
    }

    @Test
    void prePersist(){
        //given
        Team team = Team.builder()
                .name(SAMPLE_TEAM_NAME)
                .build();
        LocalDateTime testTime = LocalDateTime.now();

        //when
        team = teamRepository.save(team);

        //then
        assertNotNull(team);
        assertNotNull(team.getTimeCreated());
        assertThat(team.getTimeCreated()).isAfterOrEqualTo(testTime);
        assertThat(team.getTimeCreated()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void robotTeamPrePersist() {
        //given
        Team team = Team.builder()
                .name(SAMPLE_TEAM_NAME2)
                .build();
        team = teamRepository.save(team);

        Robot robot = robotRepository.save(Robot.builder().name(SAMPLE_ROBOT_NAME).build());

        RobotTeam robotTeam = RobotTeam.builder()
                .robot(robot)
                .team(team)
                .build();
        team.getRobotTeams().add(robotTeam);

        //when
        team = teamRepository.save(team);
        Optional<Team> teamOptional = teamRepository
                .findByNameWithRobotTeams(SAMPLE_TEAM_NAME2.toLowerCase(Locale.ROOT));

        //then
        assertThat(teamOptional).isPresent();
        assertNotNull(teamOptional.get().getRobotTeams());
        assertThat(teamOptional.get().getRobotTeams().stream().findFirst()).isPresent();
        assertNotNull(teamOptional.get().getRobotTeams().stream().findFirst().get().getTimeAdded());
        assertNotNull(teamOptional.get().getRobotTeams().stream().findFirst().get().getRobot());
        assertNotNull(teamOptional.get().getRobotTeams().stream().findFirst().get().getRobot().getTimeAdded());
    }

}