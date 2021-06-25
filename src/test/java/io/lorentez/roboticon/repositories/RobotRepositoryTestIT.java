package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.Robot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RobotRepositoryTestIT {

    @Autowired
    RobotRepository robotRepository;

    @Test
    void testPrePersist(){
        //given
        Robot robot = Robot.builder()
                .name("Sample name")
                .build();
        LocalDateTime testTime = LocalDateTime.now();

        //when
        robot = robotRepository.save(robot);

        //then
        assertNotNull(robot);
        assertNotNull(robot.getTimeAdded());
        assertThat(robot.getTimeAdded()).isAfterOrEqualTo(testTime);
        assertThat(robot.getTimeAdded()).isBeforeOrEqualTo(LocalDateTime.now());

    }

}