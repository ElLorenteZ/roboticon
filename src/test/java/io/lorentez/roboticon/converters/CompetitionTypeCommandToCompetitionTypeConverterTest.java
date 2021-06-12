package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.CompetitionTypeCommand;
import io.lorentez.roboticon.model.CompetitionType;
import io.lorentez.roboticon.model.ScoreType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CompetitionTypeCommandToCompetitionTypeConverterTest {

    private static final Long ID = 1L;
    private static final String TYPE = "Sumo standard";
    private static final ScoreType SCORE_TYPE = ScoreType.MIN_TIME;

    CompetitionTypeCommandToCompetitionTypeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new CompetitionTypeCommandToCompetitionTypeConverter();
    }

    @Test
    void testNull() {
        //given
        CompetitionTypeCommand competitionTypeCommand = null;

        //when
        CompetitionType competitionType = converter.convert(competitionTypeCommand);

        //then
        assertNull(competitionType);
    }

    @Test
    void testEmptyObject() {
        //given
        CompetitionTypeCommand competitionTypeCommand = new CompetitionTypeCommand();

        //when
        CompetitionType competitionType = converter.convert(competitionTypeCommand);

        //then
        assertNotNull(competitionType);
        assertNotNull(competitionType.getCompetitions());
        assertThat(competitionType.getCompetitions()).isEmpty();
        assertNull(competitionType.getId());
        assertNull(competitionType.getType());
        assertNull(competitionType.getScoreType());
    }

    @Test
    void testFullObject() {
        //given
        CompetitionTypeCommand competitionTypeCommand = CompetitionTypeCommand.builder()
                .id(ID)
                .type(TYPE)
                .scoreType(SCORE_TYPE)
                .build();

        //when
        CompetitionType competitionType = converter.convert(competitionTypeCommand);

        //then
        assertNotNull(competitionType);
        assertNotNull(competitionType.getCompetitions());
        assertThat(competitionType.getCompetitions()).isEmpty();
        assertEquals(ID, competitionType.getId());
        assertEquals(TYPE, competitionType.getType());
        assertEquals(SCORE_TYPE, competitionType.getScoreType());

    }
}