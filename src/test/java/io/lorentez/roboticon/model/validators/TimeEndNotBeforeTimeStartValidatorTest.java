package io.lorentez.roboticon.model.validators;

import io.lorentez.roboticon.model.Tournament;
import io.lorentez.roboticon.model.UserTeam;
import io.lorentez.roboticon.model.UserTeamStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TimeEndNotBeforeTimeStartValidator tests - ")
class TimeEndNotBeforeTimeStartValidatorTest {

    private static final Long ID = 1L;
    private static final String TOURNAMENT_NAME = "Sample Tournament";
    private static Validator validator;
    private static final LocalDate TIME_TEST = LocalDate.of(2021, 4, 14);

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    class TournamentTests {
        @Test
        void oneDayTournamentBothDatesValid() {
            //given
            Tournament tournament = Tournament.builder()
                    .id(ID)
                    .name(TOURNAMENT_NAME)
                    .dateStart(TIME_TEST)
                    .dateEnd(TIME_TEST)
                    .build();

            //when
            Set<ConstraintViolation<Tournament>> violations = validator.validate(tournament);

            //then
            assertThat(violations).hasSize(0);
        }

        @Test
        void tournamentBothDatesValid() {
            //given
            Tournament tournament = Tournament.builder()
                    .id(ID)
                    .name(TOURNAMENT_NAME)
                    .dateStart(TIME_TEST)
                    .dateEnd(TIME_TEST.plusDays(1))
                    .build();

            //when
            Set<ConstraintViolation<Tournament>> violations = validator.validate(tournament);

            //then
            assertThat(violations).hasSize(0);
        }

        @Test
        void tournamentDatesInvalid() {
            //given
            Tournament tournament = Tournament.builder()
                    .id(ID)
                    .name(TOURNAMENT_NAME)
                    .dateStart(TIME_TEST)
                    .dateEnd(TIME_TEST.minusDays(1))
                    .build();

            //when
            Set<ConstraintViolation<Tournament>> violations = validator.validate(tournament);

            //then
            assertThat(violations).hasSize(1);
        }
    }

}