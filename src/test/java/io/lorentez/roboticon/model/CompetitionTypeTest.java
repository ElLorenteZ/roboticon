package io.lorentez.roboticon.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CompetitionTypeTest {

    private static final Long ID = 1L;
    public static final String TYPE = "Sample type";

    @Test
    void builder() {
        //given

        //when
        CompetitionType competitionType = CompetitionType.builder()
                .id(ID)
                .type(TYPE)
                .build();

        //then
        assertEquals(ID, competitionType.getId());
        assertEquals(TYPE, competitionType.getType());
        assertNotNull(competitionType.getCompetitions());
        assertThat(competitionType.getCompetitions()).hasSize(0);
    }
}