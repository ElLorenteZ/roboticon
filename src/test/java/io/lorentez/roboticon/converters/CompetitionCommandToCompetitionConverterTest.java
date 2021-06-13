package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.CompetitionCommand;
import io.lorentez.roboticon.commands.CompetitionTypeCommand;
import io.lorentez.roboticon.model.Competition;
import io.lorentez.roboticon.model.CompetitionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompetitionCommandToCompetitionConverterTest {

    public static final Long ID = 15L;
    public static final String NAME = "Example name";
    public static final String DESCRIPTION = "Example description";

    @Mock
    CompetitionTypeCommandToCompetitionTypeConverter competitionTypeConverter;

    @InjectMocks
    CompetitionCommandToCompetitionConverter converter;

    @Test
    void testNull() {
        //given
        CompetitionCommand competitionCommand = null;

        //when
        Competition competition = converter.convert(competitionCommand);

        //then
        assertNull(competition);
        verifyNoInteractions(competitionTypeConverter);
    }

    @Test
    void testEmpty() {
        //given
        CompetitionCommand competitionCommand = new CompetitionCommand();
        given(competitionTypeConverter.convert(null)).willReturn(null);

        //when
        Competition competition = converter.convert(competitionCommand);

        //then
        assertNotNull(competition);
        assertNull(competition.getId());
        assertNull(competition.getName());
        assertNull(competition.getDescription());
        assertNull(competition.getCompetitionType());
        verify(competitionTypeConverter, times(1)).convert(any());
        verifyNoMoreInteractions(competitionTypeConverter);
    }

    @Test
    void testFullObject() {
        //given
        CompetitionCommand competitionCommand = CompetitionCommand.builder()
                .id(ID)
                .name(NAME)
                .description(DESCRIPTION)
                .competitionType(new CompetitionTypeCommand())
                .build();
        given(competitionTypeConverter.convert(any())).willReturn(new CompetitionType());

        //when
        Competition competition = converter.convert(competitionCommand);

        //then
        assertNotNull(competition);
        assertEquals(ID, competition.getId());
        assertEquals(NAME, competition.getName());
        assertEquals(DESCRIPTION, competition.getDescription());
        assertNotNull(competition.getCompetitionType());
        verify(competitionTypeConverter, times(1)).convert(any());
        verifyNoMoreInteractions(competitionTypeConverter);
    }
}