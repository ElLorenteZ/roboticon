package io.lorentez.roboticon.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CompetitionTypeTest {

    private static final Long ID = 1L;
    public static final String TYPE = "Sample type";

    @Test
    void builderEmptyCollections() {
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

    @Test
    void builderAndPopulateCollections() {
        //given
        Competition competition1 = Competition.builder()
                .id(1L)
                .build();
        Competition competition2 = Competition.builder()
                .id(2L)
                .build();

        //when
        CompetitionType competitionType = CompetitionType.builder()
                .id(ID)
                .type(TYPE)
                .build();
        competitionType.getCompetitions().add(competition1);
        competitionType.getCompetitions().add(competition2);

        //then
        assertEquals(ID, competitionType.getId());
        assertEquals(TYPE, competitionType.getType());
        assertNotNull(competitionType.getCompetitions());
        assertThat(competitionType.getCompetitions()).hasSize(2);
    }
}