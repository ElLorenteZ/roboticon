package io.lorentez.roboticon.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CompetitionTest {

    private static Long ID = 1L;
    private static String NAME = "Sample Competition";
    private String DESCRIPTION = "This is sample description";

    @Test
    void builder() {
        //given

        //when
        Competition competition = Competition.builder()
                .id(ID)
                .name(NAME)
                .description(DESCRIPTION)
                .build();

        //when
        assertEquals(ID, competition.getId());
        assertEquals(NAME, competition.getName());
        assertEquals(DESCRIPTION, competition.getDescription());
    }
}