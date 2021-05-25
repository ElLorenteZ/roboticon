package io.lorentez.roboticon.model;

import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TournamentTest {

    private static final Long ID = 1L;
    private static final String NAME = "Sample Tournament";
    private static final LocalDate DATE_START = LocalDate.now();
    private static final LocalDate DATE_END = LocalDate.now().plusDays(1);

    @Test
    void builder() {
        //given

        //when
        Tournament tournament = Tournament.builder()
                .id(ID)
                .name(NAME)
                .dateStart(DATE_START)
                .dateEnd(DATE_END)
                .build();

        //then
        assertEquals(ID, tournament.getId());
        assertEquals(NAME, tournament.getName());
        assertEquals(DATE_START, tournament.getDateStart());
        assertEquals(DATE_END, tournament.getDateEnd());
        assertNotNull(tournament.getCompetitions());
        assertThat(tournament.getCompetitions()).hasSize(0);
    }

}