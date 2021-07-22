package io.lorentez.roboticon.services;

import io.lorentez.roboticon.commands.UniversityCommand;
import io.lorentez.roboticon.converters.UniversityCommandToUniversityConverter;
import io.lorentez.roboticon.converters.UniversityToUniversityCommandConverter;
import io.lorentez.roboticon.model.University;
import io.lorentez.roboticon.repositories.UniversityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UniversityServiceImplTest {

    public static final Long UNIVERSITY_ID = 1L;
    public static final String UNIVERSITY_NAME = "Test name";

    @Captor
    ArgumentCaptor<University> argumentCaptor;

    @Mock
    UniversityToUniversityCommandConverter universityToUniversityCommandConverter;

    @Mock
    UniversityCommandToUniversityConverter universityCommandToUniversityConverter;

    @Mock
    UniversityRepository universityRepository;

    @InjectMocks
    UniversityServiceImpl service;

    @Test
    void testFindAll() {
        //given
        given(universityRepository.findAll()).willReturn(
                List.of(
                        University.builder().id(1L).build(),
                        University.builder().id(2L).build()
                )
        );

        //when
        List<UniversityCommand> commands = service.findAll();

        //then
        verify(universityRepository).findAll();
        verify(universityToUniversityCommandConverter, times(2)).convert(any());
        verifyNoMoreInteractions(universityRepository);
        verifyNoInteractions(universityCommandToUniversityConverter);
    }

    @Test
    void testFindByIdFound() {
        //given
        given(universityRepository.findById(anyLong())).willReturn(
                Optional.of(
                        University.builder()
                                .id(1L)
                                .build()
                )
        );

        //when
        UniversityCommand universityCommand = service.findById(1L);

        //then
        verify(universityRepository).findById(anyLong());
        verify(universityToUniversityCommandConverter).convert(any());
        verifyNoMoreInteractions(universityRepository);
        verifyNoMoreInteractions(universityToUniversityCommandConverter);
        verifyNoInteractions(universityCommandToUniversityConverter);
    }

    @Test
    void testFindByIdNotFound() {
        //given

        //when
        assertThrows(NoSuchElementException.class, () -> {
            UniversityCommand universityCommand = service.findById(1L);
        });

        verify(universityRepository).findById(anyLong());
        verifyNoInteractions(universityToUniversityCommandConverter);
        verifyNoInteractions(universityCommandToUniversityConverter);
    }

    @Test
    void testSave() {
        UniversityCommand command = UniversityCommand.builder()
                .id(UNIVERSITY_ID)
                .name(UNIVERSITY_NAME)
                .build();
        University university = University.builder()
                .id(UNIVERSITY_ID)
                .name(UNIVERSITY_NAME)
                .build();
        given(universityCommandToUniversityConverter.convert(any())).willReturn(university);

        command = service.addUniversity(command);

        verify(universityRepository).save(argumentCaptor.capture());
        University universitySaved = argumentCaptor.getValue();
        assertNull(universitySaved.getId());
        verify(universityCommandToUniversityConverter).convert(any());
        verify(universityToUniversityCommandConverter).convert(any());
        verifyNoMoreInteractions(universityRepository);
        verifyNoMoreInteractions(universityCommandToUniversityConverter);
        verifyNoMoreInteractions(universityToUniversityCommandConverter);
    }

    @Test
    void testUpdateUniversity() {
        //given
        given(universityRepository.findById(anyLong()))
                .willReturn(Optional.of(University.builder().id(UNIVERSITY_ID).build()));

        //when
        UniversityCommand command = service.update(UNIVERSITY_ID, UniversityCommand.builder()
                .name(UNIVERSITY_NAME)
                .build());

        verify(universityRepository).save(argumentCaptor.capture());
        University university = argumentCaptor.getValue();
        verify(universityRepository).findById(any());
        verify(universityToUniversityCommandConverter).convert(any());
        verifyNoInteractions(universityCommandToUniversityConverter);
        assertNotNull(university);
        assertEquals(UNIVERSITY_NAME, university.getName());
    }
}