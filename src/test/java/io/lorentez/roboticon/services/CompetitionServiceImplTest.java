package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.CompetitionCommand;
import io.lorentez.roboticon.commands.CompetitionTypeCommand;
import io.lorentez.roboticon.converters.CompetitionToCompetitionCommandConverter;
import io.lorentez.roboticon.model.Competition;
import io.lorentez.roboticon.model.CompetitionType;
import io.lorentez.roboticon.model.ScoreType;
import io.lorentez.roboticon.repositories.CompetitionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompetitionServiceImplTest {

    public static final Long COMPETITION_ID = 1L;
    public static final String COMPETITION_NAME = "Competititon name";
    public static final String COMPETITION_DESCRIPTION = "Competition desciption";
    public static final Long COMPETITION_TYPE_ID = 10L;
    public static final String COMPETITION_TYPE = "Competition type";
    public static final ScoreType COMPETITION_SCORE_TYPE = ScoreType.MIN_TIME;


    @InjectMocks
    CompetitionServiceImpl service;

    @Mock
    CompetitionRepository repository;

    @Mock
    CompetitionToCompetitionCommandConverter converter;

    @Test
    void findByIdNotFind() {
        //given
        given(repository.findWithRegistrationsById(anyLong())).willReturn(Optional.empty());

        //when
        CompetitionCommand competitionCommand = service.findById(1L);

        //then
        assertNull(competitionCommand);
        verify(repository).findWithRegistrationsById(anyLong());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findByIdFound() {
        //given
        Optional<Competition> competitionOptional = Optional.ofNullable(
                Competition.builder()
                        .id(COMPETITION_ID)
                        .name(COMPETITION_NAME)
                        .description(COMPETITION_DESCRIPTION)
                        .competitionType(CompetitionType.builder()
                                .id(COMPETITION_TYPE_ID)
                                .type(COMPETITION_TYPE)
                                .scoreType(COMPETITION_SCORE_TYPE)
                                .build())
                        .build()
        );
        given(repository.findWithRegistrationsById(anyLong())).willReturn(competitionOptional);
        given(converter.convert(any(Competition.class))).willReturn(CompetitionCommand.builder()
                .id(COMPETITION_ID)
                .name(COMPETITION_NAME)
                .description(COMPETITION_DESCRIPTION)
                .competitionType(CompetitionTypeCommand.builder()
                        .id(COMPETITION_TYPE_ID)
                        .type(COMPETITION_TYPE)
                        .scoreType(COMPETITION_SCORE_TYPE)
                        .build())
                .build());

        //when
        CompetitionCommand competitionCommand = service.findById(1L);

        //then
        assertNotNull(competitionCommand);
        assertEquals(COMPETITION_ID, competitionCommand.getId());
        verify(repository, times(1)).findWithRegistrationsById(anyLong());
        verifyNoMoreInteractions(repository);
        verify(converter, times(1)).convert(any(Competition.class));
        verifyNoMoreInteractions(converter);
    }
}