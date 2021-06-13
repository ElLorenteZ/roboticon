package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.CompetitionCommand;
import io.lorentez.roboticon.commands.TournamentCommand;
import io.lorentez.roboticon.model.Competition;
import io.lorentez.roboticon.model.Tournament;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentToTournamentCommandConverterTest {

    private static final Long ID = 100L;
    public static final String NAME = "Test Tournament";
    public static final LocalDate DATE_START = LocalDate.now().plusMonths(1);
    public static final LocalDate DATE_END = LocalDate.now().plusMonths(1).plusDays(1);

    @Mock
    CompetitionToCompetitionCommandConverter competitionConverter;

    @InjectMocks
    TournamentToTournamentCommandConverter converter;

    @Test
    void testNull() {
        //given
        Tournament tournament = null;

        //when
        TournamentCommand tournamentCommand = converter.convert(tournament);

        //then
        assertNull(tournamentCommand);
        verifyNoInteractions(competitionConverter);
    }

    @Test
    void testEmpty() {
        //given
        Tournament tournament = new Tournament();

        //when
        TournamentCommand tournamentCommand = converter.convert(tournament);

        //then
        assertNotNull(tournamentCommand);
        assertNull(tournamentCommand.getId());
        assertNull(tournamentCommand.getName());
        assertNull(tournamentCommand.getDateStart());
        assertNull(tournamentCommand.getDateEnd());
        assertThat(tournamentCommand.getCompetitions()).isEmpty();
    }

    @Test
    void testFullObject() {
        //given
        Tournament tournament = Tournament.builder()
                .id(ID)
                .name(NAME)
                .dateStart(DATE_START)
                .dateEnd(DATE_END)
                .build();
        tournament.getCompetitions().add(new Competition());
        given(competitionConverter.convert(any(Competition.class)))
                .willReturn(new CompetitionCommand());

        //when
        TournamentCommand tournamentCommand = converter.convert(tournament);

        //then
        assertNotNull(tournamentCommand);
        assertEquals(ID, tournamentCommand.getId());
        assertEquals(NAME, tournamentCommand.getName());
        assertEquals(DATE_START, tournamentCommand.getDateStart());
        assertEquals(DATE_END, tournamentCommand.getDateEnd());
        assertNotNull(tournamentCommand.getCompetitions());
        assertThat(tournamentCommand.getCompetitions()).hasSize(tournament.getCompetitions().size());
        verify(competitionConverter, times(1)).convert(any());
        verifyNoMoreInteractions(competitionConverter);
    }
}