package com.example.project;

import com.example.project.Exception.ScoreBoardException;
import com.example.project.Interface.Match;
import com.example.project.Interface.ScoreBoard;
import com.example.project.InterfaceImpl.FootballMatch;
import com.example.project.InterfaceImpl.WorldCupScoreBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorldCupScoreBoardTest {

    private ScoreBoard testScoreBoard;

    @BeforeEach
    void setUp() {
        List<Match> activeMatches = new ArrayList<>();
        testScoreBoard = new WorldCupScoreBoard(activeMatches);
    }

    @Test
    void shouldStartMatchAndInitializeScoreWithZeroToScoreBoard() {
        Team homeTeam = new Team("Mexico");
        Team awayTeam = new Team("Canada");

        testScoreBoard.addMatch(homeTeam, awayTeam);

        List<Match> matches = testScoreBoard.getMatches();

        assertFalse(matches.isEmpty());
        assertTrue(matches.stream().allMatch(match ->
                "Mexico".equals(match.getHomeTeam().getName()) &&
                        Integer.valueOf(0).equals(match.getHomeTeam().getScore()) &&
                        "Canada".equals(match.getAwayTeam().getName()) &&
                        Integer.valueOf(0).equals(match.getHomeTeam().getScore())));
    }

    @Test
    void shouldThrowErrorWhenAddedStartedMatchInScoreBoard() {
        Team homeTeam = new Team("Mexico");
        Team awayTeam = new Team("Canada");
        testScoreBoard.addMatch(homeTeam, awayTeam);

        assertThatThrownBy(() -> testScoreBoard.addMatch(homeTeam, awayTeam))
                .isInstanceOf(ScoreBoardException.class)
                .hasMessageContaining("Oops! The match has already started");
    }

    @Test
    void shouldUpdateMatchInScoreBoard() {
        Team homeTeam = new Team("Mexico");
        Team awayTeam =  new Team("Canada");
        int homeTeamScore = 1;
        int awayTeamScore = 0;

        testScoreBoard.addMatch(homeTeam, awayTeam);

        assertEquals(0, homeTeam.getScore());
        assertEquals(0, awayTeam.getScore());

        testScoreBoard.updateMatch(homeTeamScore, awayTeamScore);

        assertEquals(homeTeamScore, homeTeam.getScore());
        assertEquals(awayTeamScore, awayTeam.getScore());
    }

    @Test
    void shouldNotUpdateMatchWhenMatchIsInactiveInScoreBoard() {
        Team homeTeam = new Team("Mexico");
        Team awayTeam =  new Team("Canada");

        testScoreBoard.addMatch(homeTeam, awayTeam);
        testScoreBoard.updateMatch(0, 1);
        testScoreBoard.finishMatch();

        assertThatThrownBy(() -> testScoreBoard.updateMatch(1,1))
                .isInstanceOf(ScoreBoardException.class)
                .hasMessageContaining("There is no ongoing match to update score");
    }

    @Test
    void shouldThrowExceptionWhenFinishIsCalledForUnPlayedMatchInScoreBoard() {
        assertThatThrownBy(() -> testScoreBoard.finishMatch())
                .isInstanceOf(ScoreBoardException.class)
                .hasMessageContaining("There is no ongoing match to finish");
    }

    @Test
    void ShouldFinishMatchInScoreBoard() {
        Team homeTeam = new Team("Mexico");
        Team awayTeam =  new Team("Canada");

        testScoreBoard.addMatch(homeTeam, awayTeam);
        testScoreBoard.finishMatch();

        assertTrue(testScoreBoard.getMatches().isEmpty());
    }

    @Test
    void shouldReturnMatchesInSortedOrderBasedOnMaximumScoredGoals() {
        Team home1 = new Team("Mexico",0);
        Team away1 = new Team("Canada",5);

        Team home2 = new Team("Spain",10);
        Team away2 = new Team("Brazil",2);

        Team home3 = new Team("Germany",2);
        Team away3 = new Team("France",2);

        Team home4 = new Team("Uruguay",6);
        Team away4 = new Team("Italy",6);

        Team home5 = new Team("Argentina",3);
        Team away5 = new Team("Australia",1);

        testScoreBoard.addMatch(home1, away1);
        testScoreBoard.addMatch(home2, away2);
        testScoreBoard.addMatch(home3, away3);
        testScoreBoard.addMatch(home4, away4);
        testScoreBoard.addMatch(home5, away5);

        List<Match> sortedFootballMatches = testScoreBoard.getMatchesInSortedOrderBasedOnMaximumScoredGoals();

        assertFalse(sortedFootballMatches.isEmpty());
        assertEquals(5, sortedFootballMatches.size());

        //1. Uruguay 6 - Italy 6
        assertEquals(new FootballMatch(home4, away4), sortedFootballMatches.get(0));
        //2. Spain 10 - Brazil 2
        assertEquals(new FootballMatch(home2, away2), sortedFootballMatches.get(1));
        //3. Mexico 0 - Canada 5
        assertEquals(new FootballMatch(home1, away1), sortedFootballMatches.get(2));
        //4. Argentina 3 - Australia 1
        assertEquals(new FootballMatch(home5, away5), sortedFootballMatches.get(3));
        //5. Germany 2 - France 2
        assertEquals(new FootballMatch(home3, away3), sortedFootballMatches.get(4));
    }

}
