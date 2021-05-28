package io.lorentez.roboticon.model.validators;

import io.lorentez.roboticon.model.RegistrationCurrentStatus;
import io.lorentez.roboticon.model.RegistrationStatus;
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
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TimeEndAfterTimeStartValidator tests - ")
class TimeEndAfterTimeStartValidatorTest {

    private static final Long ID = 1L;
    private static final UserTeamStatus USER_TEAM_STATUS = UserTeamStatus.MEMBER;
    private static Validator validator;
    public static final LocalDateTime TIME_TEST = LocalDateTime.of(2021, 4, 14, 12, 30, 7);

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("UserTeam - ")
    class UserTeamTests {
        @Test
        void userTeamBothDatesValid() {
            //given
            UserTeam userTeam = UserTeam.builder()
                    .id(ID)
                    .status(USER_TEAM_STATUS)
                    .timeAdded(TIME_TEST)
                    .timeRemoved(TIME_TEST.plusHours(1L))
                    .build();

            //when
            Set<ConstraintViolation<UserTeam>> violations = validator.validate(userTeam);

            //then
            assertThat(violations).hasSize(0);
        }

        @Test
        void userTeamOnlyDateStart() {
            //given
            UserTeam userTeam = UserTeam.builder()
                    .id(ID)
                    .status(USER_TEAM_STATUS)
                    .timeAdded(TIME_TEST)
                    .build();

            //when
            Set<ConstraintViolation<UserTeam>> violations = validator.validate(userTeam);

            //then
            assertThat(violations).hasSize(0);
        }

        @Test
        void userTeamDatesInvalid() {
            //given
            UserTeam userTeam = UserTeam.builder()
                    .id(ID)
                    .status(USER_TEAM_STATUS)
                    .timeAdded(TIME_TEST)
                    .timeRemoved(TIME_TEST.minusHours(1L))
                    .build();

            //when
            Set<ConstraintViolation<UserTeam>> violations = validator.validate(userTeam);

            //then
            assertThat(violations).hasSize(1);
        }
    }

    @Nested
    @DisplayName("RegistrationStatus - ")
    class RegistrationStatusTests {

        @Test
        void registrationStatusBothDatesValid(){
            //given
            RegistrationStatus registrationStatus = RegistrationStatus.builder()
                    .id(ID)
                    .status(RegistrationCurrentStatus.APPLIED)
                    .timeFrom(TIME_TEST)
                    .timeTo(TIME_TEST.plusHours(1))
                    .build();

            //when
            Set<ConstraintViolation<RegistrationStatus>> violations = validator.validate(registrationStatus);

            //then
            assertThat(violations).hasSize(0);
        }

        @Test
        void registrationStatusOnlyDateStart() {
            //given
            RegistrationStatus registrationStatus = RegistrationStatus.builder()
                    .id(ID)
                    .status(RegistrationCurrentStatus.APPLIED)
                    .timeFrom(TIME_TEST)
                    .build();

            //when
            Set<ConstraintViolation<RegistrationStatus>> violations = validator.validate(registrationStatus);

            //then
            assertThat(violations).hasSize(0);
        }

        @Test
        void registrationStatusDatesInvalid() {
            //given
            RegistrationStatus registrationStatus = RegistrationStatus.builder()
                    .id(ID)
                    .status(RegistrationCurrentStatus.APPLIED)
                    .timeFrom(TIME_TEST)
                    .timeTo(TIME_TEST.minusHours(1))
                    .build();

            //when
            Set<ConstraintViolation<RegistrationStatus>> violations = validator.validate(registrationStatus);

            //then
            assertThat(violations).hasSize(1);
        }
    }


}