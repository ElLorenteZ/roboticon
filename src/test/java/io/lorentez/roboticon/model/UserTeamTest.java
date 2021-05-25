package io.lorentez.roboticon.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTeamTest {

    public static final Long ID = 1L;
    private static final UserTeamStatus STATUS = UserTeamStatus.MEMBER;
    private static final LocalDateTime TIME_ADDED = LocalDateTime.now();

    @Test
    void builder() {
        //given

        //when
        UserTeam userTeam = UserTeam.builder()
                .id(ID)
                .status(STATUS)
                .timeAdded(TIME_ADDED)
                .build();

        //then
        assertEquals(ID, userTeam.getId());
        assertEquals(STATUS, userTeam.getStatus());
        assertEquals(TIME_ADDED, userTeam.getTimeAdded());
        assertNull(userTeam.getTimeRemoved());
    }
}