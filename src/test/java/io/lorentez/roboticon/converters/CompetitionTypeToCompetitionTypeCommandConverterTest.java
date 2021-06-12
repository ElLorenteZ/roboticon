package io.lorentez.roboticon.converters;

import io.lorentez.roboticon.commands.CompetitionTypeCommand;
import io.lorentez.roboticon.model.CompetitionType;
import io.lorentez.roboticon.model.ScoreType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompetitionTypeToCompetitionTypeCommandConverterTest {

    private static final Long ID = 1L;
    private static final String TYPE = "Sumo Standard";
    private static ScoreType SCORE_TYPE = ScoreType.MIN_TIME;

    CompetitionTypeToCompetitionTypeCommandConverter converter;

    @BeforeEach
    void setUp() {
        converter = new CompetitionTypeToCompetitionTypeCommandConverter();
    }

    @Test
    void testNull() {
        //given
        CompetitionType competitionType = null;

        //when
        CompetitionTypeCommand competitionTypeCommand = converter.convert(competitionType);

        //then
        assertNull(competitionTypeCommand);
    }

    @Test
    void testEmptyObject() {
        //given
        CompetitionType competitionType = new CompetitionType();

        //when
        CompetitionTypeCommand competitionTypeCommand = converter.convert(competitionType);

        //then
        assertNotNull(competitionTypeCommand);
        assertNull(competitionTypeCommand.getType());
        assertNull(competitionTypeCommand.getScoreType());
        assertNull(competitionTypeCommand.getId());
    }

    @Test
    void testFullObject() {
        //given
        CompetitionType competitionType = CompetitionType.builder()
                .id(ID)
                .scoreType(SCORE_TYPE)
                .type(TYPE)
                .build();

        //when
        CompetitionTypeCommand competitionTypeCommand = converter.convert(competitionType);

        //then
        assertNotNull(competitionTypeCommand);
        assertEquals(ID, competitionTypeCommand.getId());
        assertEquals(TYPE, competitionTypeCommand.getType());
        assertEquals(SCORE_TYPE, competitionTypeCommand.getScoreType());
    }
}