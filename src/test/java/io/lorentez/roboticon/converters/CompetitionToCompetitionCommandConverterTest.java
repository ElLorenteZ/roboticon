package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.CompetitionCommand;
import io.lorentez.roboticon.commands.CompetitionTypeCommand;
import io.lorentez.roboticon.model.Competition;
import io.lorentez.roboticon.model.CompetitionType;
import io.lorentez.roboticon.model.Registration;
import io.lorentez.roboticon.model.ScoreType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompetitionToCompetitionCommandConverterTest {

    public static final Long ID = 1L;
    public static final String NAME = "LEGO Line Follower";
    public static final String DESCRIPTION = "Sample description";

    @Mock
    CompetitionTypeToCompetitionTypeCommandConverter competitionTypeConverter;

    @InjectMocks
    CompetitionToCompetitionCommandConverter converter;

    @Test
    void testNull() {
        //given
        Competition competition = null;

        //when
        CompetitionCommand competitionCommand = converter.convert(competition);

        //then
        assertNull(competitionCommand);
        verifyNoInteractions(competitionTypeConverter);
    }

    @Test
    void testEmpty() {
        //given
        Competition competition = new Competition();
        given(competitionTypeConverter.convert(null)).willReturn(null);

        //when
        CompetitionCommand competitionCommand = converter.convert(competition);

        //then
        assertNotNull(competitionCommand);
        verify(competitionTypeConverter, times(1)).convert(any());
        verifyNoMoreInteractions(competitionTypeConverter);
    }

    @Test
    void testFullObject() {
        //given
        Competition competition = Competition.builder()
                .id(ID)
                .name(NAME)
                .description(DESCRIPTION)
                .competitionType(new CompetitionType())
                .registrations(Set.of(Registration.builder().id(1L).build()))
                .build();
        given(competitionTypeConverter.convert(any(CompetitionType.class))).willReturn(new CompetitionTypeCommand());

        //when
        CompetitionCommand competitionCommand = converter.convert(competition);

        //then
        assertNotNull(competitionCommand);
        assertEquals(ID, competitionCommand.getId());
        assertEquals(NAME, competitionCommand.getName());
        assertEquals(DESCRIPTION, competitionCommand.getDescription());
        assertNotNull(competitionCommand.getCompetitionType());
        assertEquals(1, competitionCommand.getRegistrationsCounter());
        verify(competitionTypeConverter, times(1)).convert(any());
        verifyNoMoreInteractions(competitionTypeConverter);
    }
}