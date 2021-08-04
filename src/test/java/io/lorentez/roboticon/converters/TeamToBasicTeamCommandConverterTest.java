package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.BasicTeamCommand;
import io.lorentez.roboticon.commands.UniversityCommand;
import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.model.University;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamToBasicTeamCommandConverterTest {

    public static final Long ID = 2341L;
    public static final String NAME = "Sample Name";
    public static final LocalDateTime TIME_CREATED = LocalDateTime.now().minusDays(1);

    @Mock
    UniversityToUniversityCommandConverter universityConverter;

    @InjectMocks
    TeamToBasicTeamCommandConverter converter;

    @Test
    void testNullObject() {
        //given

        //when
        BasicTeamCommand basicTeamCommand = converter.convert(null);

        //then
        assertNull(basicTeamCommand);
        verifyNoInteractions(universityConverter);
    }

    @Test
    void testEmptyObject() {
        //given
        Team team = Team.builder().build();

        //when
        BasicTeamCommand basicTeamCommand = converter.convert(team);

        //then
        assertNotNull(basicTeamCommand);
        verify(universityConverter, times(1)).convert(any());
        verifyNoMoreInteractions(universityConverter);
    }

    @Test
    void testFullObject(){
        //given
        UniversityCommand universityCommand = UniversityCommand.builder()
                .id(60L)
                .build();
        Team team = Team.builder()
                .id(ID)
                .name(NAME)
                .university(University.builder()
                        .id(60L)
                        .build())
                .timeCreated(TIME_CREATED)
                .build();
        given(universityConverter.convert(any(University.class))).willReturn(universityCommand);

        //when
        BasicTeamCommand basicTeamCommand = converter.convert(team);

        //then
        assertNotNull(basicTeamCommand);
        assertEquals(ID, basicTeamCommand.getId());
        assertEquals(NAME, basicTeamCommand.getName());
        assertEquals(TIME_CREATED, basicTeamCommand.getTimeCreated());
        assertNotNull(basicTeamCommand.getUniversity());
        verify(universityConverter, times(1)).convert(any(University.class));
        verifyNoMoreInteractions(universityConverter);
    }
}