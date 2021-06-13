package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.TournamentCommand;
import io.lorentez.roboticon.converters.TournamentToTournamentCommandConverter;
import io.lorentez.roboticon.model.Competition;
import io.lorentez.roboticon.model.Tournament;
import io.lorentez.roboticon.repositories.TournamentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentServiceImplTest {

    @Mock
    TournamentToTournamentCommandConverter converter;

    @Mock
    TournamentRepository repository;

    @InjectMocks
    TournamentServiceImpl service;

    @Test
    void testFindAll() {
        //given
        List<Tournament> tournaments = new ArrayList<>();
        Tournament tournament1 = Tournament.builder()
                .id(1L)
                .build();
        tournaments.add(tournament1);

        //when
        when(converter.convert(any())).thenReturn(TournamentCommand.builder().id(1L).build());
        when(repository.findAllWithCompetitions()).thenReturn(tournaments);
        List<TournamentCommand> tournamentCommands = service.findAll();

        //then
        assertNotNull(tournamentCommands);
        assertThat(tournamentCommands).hasSize(1);
        verify(repository, times(1)).findAllWithCompetitions();
        verify(converter, times(1)).convert(any());
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(converter);
    }

}