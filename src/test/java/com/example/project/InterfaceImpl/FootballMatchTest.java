package com.example.project.InterfaceImpl;

import com.example.project.Model.Team;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FootballMatchTest {

    @Test
    void shouldInitializeFootballMatchWithHomeTeamAwayTeamAndActiveStatus() {
        Team homeTeam = new Team("Mexico");
        Team awayTeam = new Team("Canada");

        FootballMatch match = new FootballMatch(homeTeam, awayTeam);

        assertEquals(homeTeam, match.getHomeTeam());
        assertEquals(awayTeam, match.getAwayTeam());
        assertTrue(match.isActive());
        assertNotNull(match.getStartTime());
    }

    @Test
    void shouldReturnTrueWhenMatchesAreEqual() {
        Team homeTeam1 = new Team("Mexico");
        Team awayTeam1 = new Team("Canada");
        FootballMatch match1 = new FootballMatch(homeTeam1, awayTeam1);

        Team homeTeam2 = new Team("Mexico");
        Team awayTeam2 = new Team("Canada");
        FootballMatch match2 = new FootballMatch(homeTeam2, awayTeam2);

        assertEquals(match1, match2);
    }

    @Test
    void shouldReturnFalseWhenMatchesAreDifferent() {
        Team homeTeam1 = new Team("Mexico");
        Team awayTeam1 = new Team("Canada");
        FootballMatch match1 = new FootballMatch(homeTeam1, awayTeam1);

        Team homeTeam2 = new Team("Brazil");
        Team awayTeam2 = new Team("Argentina");
        FootballMatch match2 = new FootballMatch(homeTeam2, awayTeam2);

        assertNotEquals(match1, match2);
    }

    @Test
    void shouldReturnTotalScoreForMatch() {
        Team homeTeam = new Team("Mexico", 2);
        Team awayTeam = new Team("Canada", 3);

        FootballMatch match = new FootballMatch(homeTeam, awayTeam);

        assertEquals(5, match.getScore());
    }

    @Test
    void shouldReturnStartTimeOfMatch() {
        Team homeTeam = new Team("Mexico");
        Team awayTeam = new Team("Canada");

        FootballMatch match = new FootballMatch(homeTeam, awayTeam);

        assertNotNull(match.getStartTime());
    }

    @Test
    void shouldReturnTrueForActiveMatch() {
        Team homeTeam = new Team("Mexico");
        Team awayTeam = new Team("Canada");

        FootballMatch match = new FootballMatch(homeTeam, awayTeam);

        assertTrue(match.isActive());
    }

    @Test
    void shouldReturnFalseForNullOrDifferentClass() {
        Team homeTeam = new Team("Mexico");
        Team awayTeam = new Team("Canada");
        FootballMatch match = new FootballMatch(homeTeam, awayTeam);

        assertNotEquals(null, match);
        assertNotEquals(new Object(), match);
    }
}
