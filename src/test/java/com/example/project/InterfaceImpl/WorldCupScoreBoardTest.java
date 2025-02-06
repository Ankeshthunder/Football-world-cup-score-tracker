package com.example.project.InterfaceImpl;

import com.example.project.Exception.ScoreBoardException;
import com.example.project.Interface.Match;
import com.example.project.Interface.ScoreBoard;
import com.example.project.Model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

public class WorldCupScoreBoardTest {

    private ScoreBoard testScoreBoard;

    @BeforeEach
    void setUp() {
        List<Match> activeMatches = new ArrayList<>();
        testScoreBoard = new WorldCupScoreBoard(activeMatches);
    }

    static Stream<List<FootballMatch>> provideMatchDataOngoingOrFinishedGame() {
        return Stream.of(
                List.of(
                        new FootballMatch(new Team("Mexico", 0), new Team("Canada", 5)),
                        new FootballMatch(new Team("Spain", 10), new Team("Brazil", 2)),
                        new FootballMatch(new Team("Germany", 2), new Team("France", 2)),
                        new FootballMatch(new Team("Uruguay", 6), new Team("Italy", 6)),
                        new FootballMatch(new Team("Argentina", 3), new Team("Australia", 1))
                )
        );
    }

    static Stream<List<FootballMatch>> provideMatchDataForScheduledGame() {
        return Stream.of(
                List.of(
                        new FootballMatch(new Team("Mexico"), new Team("Canada")),
                        new FootballMatch(new Team("Spain"), new Team("Brazil"))));
    }

    @ParameterizedTest
    @MethodSource("provideMatchDataForScheduledGame")
    void shouldStartMatchAndInitializeScoreWithZeroToScoreBoard(List<FootballMatch> matches) {
        for (FootballMatch match : matches) {
            testScoreBoard.addMatch(match.getHomeTeam(), match.getAwayTeam(), null);
        }

        List<Match> activeMatches = testScoreBoard.getMatches();

        assertEquals(2, activeMatches.size());

        assertTrue(activeMatches.stream().allMatch(match ->
                match.getHomeTeam().getScore() == 0 &&
                        match.getAwayTeam().getScore() == 0));
    }

    @Test
    void shouldThrowErrorWhenAddedStartedMatchInScoreBoard() {
        Team homeTeam = new Team("Mexico");
        Team awayTeam = new Team("Canada");

        testScoreBoard.addMatch(homeTeam, awayTeam, null);

        assertThatThrownBy(() -> testScoreBoard.addMatch(homeTeam, awayTeam, null))
                .isInstanceOf(ScoreBoardException.class)
                .hasMessageContaining("Oops! The match has already started");
    }

    @Test
    void shouldReturnSameStartTimeForTwoMatchesStartingAtSameTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        LocalDateTime fixedTime = LocalDateTime.parse("2025-02-05T08:53:15.058499", formatter);

        testScoreBoard.addMatch(new Team("Spain"), new Team("Mexico"), fixedTime);
        testScoreBoard.addMatch(new Team("China"), new Team("Japan"), fixedTime);

        List<Match> scoreBoardSummary = testScoreBoard.getMatches();
        assertEquals(fixedTime, scoreBoardSummary.get(0).getStartTime());
        assertEquals(fixedTime, scoreBoardSummary.get(1).getStartTime());
    }


    @ParameterizedTest
    @MethodSource("provideMatchDataOngoingOrFinishedGame")
    void shouldUpdateMatchScoreInScoreBoard(List<FootballMatch> matches) {
        FootballMatch match = matches.get(0);
        Team homeTeam = match.getHomeTeam();
        Team awayTeam = match.getAwayTeam();

        testScoreBoard.addMatch(homeTeam, awayTeam, null);

        assertEquals(0, homeTeam.getScore());
        assertEquals(5, awayTeam.getScore());

        testScoreBoard.updateMatch(1, 5);

        assertEquals(1, homeTeam.getScore());
        assertEquals(5, awayTeam.getScore());
    }

    @ParameterizedTest
    @MethodSource("provideMatchDataOngoingOrFinishedGame")
    void shouldNotUpdateMatchScoreWhenMatchIsInactiveInScoreBoard(List<FootballMatch> matches) {
        FootballMatch match = matches.get(0);
        Team homeTeam = match.getHomeTeam();
        Team awayTeam = match.getAwayTeam();

        testScoreBoard.addMatch(homeTeam, awayTeam, null);
        testScoreBoard.updateMatch(1, 5);
        testScoreBoard.finishMatch();

        assertThatThrownBy(() -> testScoreBoard.updateMatch(2, 5))
                .isInstanceOf(ScoreBoardException.class)
                .hasMessageContaining("There is no ongoing match to update score");
    }


    @Test
    void shouldThrowExceptionWhenFinishIsCalledForInactiveMatchInScoreBoard() {
        assertThatThrownBy(() -> testScoreBoard.finishMatch())
                .isInstanceOf(ScoreBoardException.class)
                .hasMessageContaining("There is no ongoing match to finish");
    }

    @ParameterizedTest
    @MethodSource("provideMatchDataOngoingOrFinishedGame")
    void shouldFinishMatchInScoreBoard(List<FootballMatch> matches) {
        FootballMatch match = matches.get(0);

        testScoreBoard.addMatch(match.getHomeTeam(), match.getAwayTeam(), null);
        testScoreBoard.finishMatch();

        assertTrue(testScoreBoard.getMatches().isEmpty());
    }


}
