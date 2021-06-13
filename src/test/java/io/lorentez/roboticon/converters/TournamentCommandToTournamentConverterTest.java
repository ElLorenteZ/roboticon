package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.CompetitionCommand;
import io.lorentez.roboticon.commands.TournamentCommand;
import io.lorentez.roboticon.model.Competition;
import io.lorentez.roboticon.model.Tournament;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentCommandToTournamentConverterTest {

    public static final Long ID = 15L;
    public static final String NAME = "Golden boots tournament";
    public static final LocalDate DATE_START = LocalDate.now().plusMonths(1).plusDays(1);
    public static final LocalDate DATE_END = LocalDate.now().plusMonths(1).plusDays(2);

    @Mock
    CompetitionCommandToCompetitionConverter competitionConverter;

    @InjectMocks
    TournamentCommandToTournamentConverter converter;

    @Test
    void testNull() {
        //given
        TournamentCommand tournamentCommand = null;

        //when
        Tournament tournament = converter.convert(tournamentCommand);

        //then
        assertNull(tournament);
    }

    @Test
    void testEmpty() {
        //given
        TournamentCommand tournamentCommand = new TournamentCommand();

        //when
        Tournament tournament = converter.convert(tournamentCommand);

        //then
        assertNotNull(tournament);
        assertNull(tournament.getId());
        assertNull(tournament.getName());
        assertNull(tournament.getDateStart());
        assertNull(tournament.getDateEnd());
        assertNotNull(tournament.getCompetitions());
        assertThat(tournament.getCompetitions()).isEmpty();
    }

    @Test
    void fullObject() {
        //given
        TournamentCommand tournamentCommand = TournamentCommand.builder()
                .id(ID)
                .name(NAME)
                .dateStart(DATE_START)
                .dateEnd(DATE_END)
                .build();
        tournamentCommand.getCompetitions().add(new CompetitionCommand());
        given(competitionConverter.convert(any(CompetitionCommand.class))).willReturn(new Competition());

        //when
        Tournament tournament = converter.convert(tournamentCommand);

        //then
        assertNotNull(tournament);
        assertEquals(ID, tournament.getId());
        assertEquals(NAME, tournament.getName());
        assertEquals(DATE_START, tournament.getDateStart());
        assertEquals(DATE_END, tournament.getDateEnd());
        assertNotNull(tournament.getCompetitions());
        assertThat(tournament.getCompetitions()).hasSize(1);
        verify(competitionConverter, times(1)).convert(any());
        verifyNoMoreInteractions(competitionConverter);
    }
}