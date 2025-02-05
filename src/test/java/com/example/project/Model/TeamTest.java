package com.example.project.Model;

import com.example.project.Exception.ScoreException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeamTest {


    @Test
    void shouldReturnTeamNameWhenGotInitialized() {
        String expectedTeamName = "Poland";
        Team homeTeam = new Team(expectedTeamName);

        assertEquals(expectedTeamName, homeTeam.getName());
    }

    @Test
    void shouldReturnScoreZeroWhenScoreIsNotProvided() {
        Team homeTeam = new Team("TestTeam");

        assertEquals(0, homeTeam.getScore());
    }

    @Test
    void shouldSetScoreWhenScoreIsProvided() {
        int expectedScore = 1;
        Team homeTeam = new Team("TestTeam");
        homeTeam.setScore(expectedScore);

        assertEquals(expectedScore, homeTeam.getScore());
    }

    @Test
    void shouldReturnScoreAndNameWhenBothIsProvided() {
        Team homeTeam = new Team("TestTeam", 5);

        assertEquals(5, homeTeam.getScore());
        assertEquals("TestTeam", homeTeam.getName());

    }

    @Test
    void shouldThrowExceptionWhenProvidedScoreIsNegative() {
        int wrongScore = -5;
        Team homeTeam = new Team("TestTeam");

        assertThatThrownBy(() -> homeTeam.setScore(wrongScore))
                .isInstanceOf(ScoreException.class)
                .hasMessageContaining("Score should not be negative");
    }

    @Test
    void shouldThrowExceptionWhenProvidedScoreIsLessThanPreviousOne() {
        Team homeTeam = new Team("TestTeam");
        homeTeam.setScore(1);

        assertThatThrownBy(() -> homeTeam.setScore(0))
                .isInstanceOf(ScoreException.class)
                .hasMessageContaining("Score should not be less than previous one");
    }

    @Test
    void shouldThrowExceptionWhenProvidedScoreIsGreaterByMoreThanOneThanPreviousOne() {
        Team homeTeam = new Team("TestTeam");

        assertThatThrownBy(() -> homeTeam.setScore(2))
                .isInstanceOf(ScoreException.class)
                .hasMessageContaining("Score should not be greater by more than one than previous score");
    }

}
