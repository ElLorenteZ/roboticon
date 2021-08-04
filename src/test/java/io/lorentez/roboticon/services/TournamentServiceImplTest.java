package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.CompetitionCommand;
import io.lorentez.roboticon.commands.CompetitionTypeCommand;
import io.lorentez.roboticon.commands.TournamentCommand;
import io.lorentez.roboticon.converters.CompetitionCommandToCompetitionConverter;
import io.lorentez.roboticon.converters.TournamentCommandToTournamentConverter;
import io.lorentez.roboticon.converters.TournamentToTournamentCommandConverter;
import io.lorentez.roboticon.model.Competition;
import io.lorentez.roboticon.model.CompetitionType;
import io.lorentez.roboticon.model.Tournament;
import io.lorentez.roboticon.repositories.CompetitionRepository;
import io.lorentez.roboticon.repositories.CompetitionTypeRepository;
import io.lorentez.roboticon.repositories.TournamentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentServiceImplTest {

    public static final long ID = 1L;
    public static final String TOURNAMENT_NAME = "Sample tournament";
    public static final LocalDate TOURNAMENT_DATESTART = LocalDate.now().plusDays(10);
    public static final LocalDate TOURNAMENT_DATEEND = LocalDate.now().plusDays(11);

    @Mock
    TournamentToTournamentCommandConverter tournamentToTournamentCommandConverter;

    @Mock
    TournamentCommandToTournamentConverter tournamentCommandToTournamentConverter;

    @Mock
    CompetitionRepository competitionRepository;

    @Mock
    CompetitionTypeRepository competitionTypeRepository;

    @Mock
    CompetitionCommandToCompetitionConverter competitionCommandToCompetitionConverter;

    @Mock
    TournamentRepository repository;

    @InjectMocks
    TournamentServiceImpl service;

    @Captor
    ArgumentCaptor<Tournament> tournamentArgumentCaptor;

    @Test
    void testFindAll() {
        //given
        List<Tournament> tournaments = new ArrayList<>();
        Tournament tournament1 = Tournament.builder()
                .id(1L)
                .build();
        tournaments.add(tournament1);
        given(tournamentToTournamentCommandConverter.convert(any())).willReturn(TournamentCommand.builder().id(1L).build());
        given(repository.findAllWithCompetitions()).willReturn(tournaments);

        //when
        List<TournamentCommand> tournamentCommands = service.findAll();

        //then
        assertNotNull(tournamentCommands);
        assertThat(tournamentCommands).hasSize(1);
        verify(repository, times(1)).findAllWithCompetitions();
        verify(tournamentToTournamentCommandConverter, times(1)).convert(any());
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(tournamentToTournamentCommandConverter);
    }

    @Test
    void findByIdNotFound() {
        //given
        given(repository.findById(anyLong())).willReturn(Optional.empty());

        //when
        TournamentCommand tournamentCommand = service.findById(1L);

        //then
        assertNull(tournamentCommand);
        verify(repository).findById(anyLong());
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(tournamentToTournamentCommandConverter);
    }

    @Test
    void findByIdSuccess() {
        //given
        Tournament tournament = Tournament.builder()
                .id(ID)
                .name(TOURNAMENT_NAME)
                .dateStart(TOURNAMENT_DATESTART)
                .build();
        given(repository.findById(anyLong())).willReturn(Optional.of(tournament));
        given(tournamentToTournamentCommandConverter.convert(tournament)).willReturn(TournamentCommand.builder()
                .id(ID)
                .name(TOURNAMENT_NAME)
                .dateStart(TOURNAMENT_DATESTART)
                .build());

        //when
        TournamentCommand tournamentCommand = service.findById(ID);

        //then
        assertNotNull(tournamentCommand);
        assertEquals(ID, tournamentCommand.getId());
        assertEquals(TOURNAMENT_NAME, tournamentCommand.getName());
        verify(repository).findById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testTournamentSave() {
        TournamentCommand tournamentCommand = TournamentCommand.builder()
                .id(10L)
                .competitions(Set.of(CompetitionCommand.builder()
                        .id(100L)
                        .competitionType(CompetitionTypeCommand.builder().id(10L).build())
                        .build()))
                .build();
        given(tournamentCommandToTournamentConverter.convert(any()))
                .willReturn(Tournament.builder().id(10L).build());
        given(repository.save(any())).willReturn(Tournament.builder()
                .id(10L)
                .competitions(Set.of(Competition.builder()
                        .id(100L)
                        .competitionType(CompetitionType.builder().id(10L).build())
                        .build()))
                .build());

        service.save(tournamentCommand);

        verify(repository).save(any());
        verify(tournamentCommandToTournamentConverter).convert(any());
        verify(tournamentToTournamentCommandConverter).convert(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testTournamentUpdate() {
        Tournament tournament = Tournament.builder()
                .id(ID)
                .name(TOURNAMENT_NAME)
                .dateStart(TOURNAMENT_DATESTART)
                .dateEnd(TOURNAMENT_DATEEND)
                .competitions(Set.of(
                        Competition.builder()
                                .id(1L)
                                .competitionType(CompetitionType.builder().id(1L).build())
                                .build(),
                        Competition.builder()
                                .id(2L)
                                .competitionType(CompetitionType.builder().id(1L).build())
                                .build(),
                        Competition.builder()
                                .id(3L)
                                .competitionType(CompetitionType.builder().id(2L).build())
                                .build()
                        ))
                .build();
        TournamentCommand tournamentCommand = TournamentCommand.builder().id(ID)
                .name(TOURNAMENT_NAME)
                .dateStart(TOURNAMENT_DATESTART)
                .dateEnd(TOURNAMENT_DATEEND)
                .competitions(Set.of(
                        CompetitionCommand.builder()
                                .id(1L)
                                .competitionType(CompetitionTypeCommand.builder().id(1L).build())
                                .build(),
                        CompetitionCommand.builder()
                                .competitionType(CompetitionTypeCommand.builder().id(1L).build())
                                .build(),
                        CompetitionCommand.builder()
                                .id(3L)
                                .competitionType(CompetitionTypeCommand.builder().id(1L).build())
                                .build()
                ))
                .build();
        given(repository.findByIdFetchCompetitions(anyLong())).willReturn(Optional.of(tournament));
        given(competitionTypeRepository.findById(anyLong()))
                .willReturn(Optional.of(CompetitionType.builder().id(1L).build()));
        given(competitionCommandToCompetitionConverter.convert(any()))
                .willReturn(Competition.builder()
                        .competitionType(CompetitionType.builder().id(1L).build())
                        .build());
        service.update(ID, tournamentCommand);

        verify(tournamentToTournamentCommandConverter).convert(tournamentArgumentCaptor.capture());
        verify(repository).findByIdFetchCompetitions(anyLong());
        verify(competitionRepository).delete(any());
        verify(competitionTypeRepository, times(2)).findById(any());
        verify(repository).save(any());
        verify(competitionRepository, times(3)).save(any());
    }



}