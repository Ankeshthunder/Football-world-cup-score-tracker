package com.example.project.InterfaceImpl;

import com.example.project.Exception.ScoreBoardException;
import com.example.project.Exception.ScoreException;
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
        FootballMatch match1= matches.get(0);
        FootballMatch match2 = matches.get(1);
        Team homeTeam1 = match1.getHomeTeam();
        Team awayTeam1 = match1.getAwayTeam();
        Team homeTeam2 = match2.getHomeTeam();
        Team awayTeam2 = match2.getAwayTeam();

        testScoreBoard.addMatch(homeTeam1, awayTeam1, null);
        testScoreBoard.addMatch(homeTeam2, awayTeam2, null);

        testScoreBoard.updateMatch(1, 5, homeTeam1.getName(), awayTeam1.getName());

        assertEquals(1, homeTeam1.getScore());
        assertEquals(5, awayTeam1.getScore());
    }

    @ParameterizedTest
    @MethodSource("provideMatchDataOngoingOrFinishedGame")
    void shouldNotUpdateMatchScoreWhenMatchIsInactiveInScoreBoard(List<FootballMatch> matches) {
        FootballMatch match = matches.get(0);
        Team homeTeam = match.getHomeTeam();
        Team awayTeam = match.getAwayTeam();

        testScoreBoard.addMatch(homeTeam, awayTeam, null);
        testScoreBoard.finishMatch(homeTeam.getName(), awayTeam.getName());

        assertThatThrownBy(() -> testScoreBoard.updateMatch(1, 5, homeTeam.getName(), awayTeam.getName()))
                .isInstanceOf(ScoreBoardException.class)
                .hasMessageContaining("There is no ongoing match to update score");
    }

    @ParameterizedTest
    @MethodSource("provideMatchDataOngoingOrFinishedGame")
    void shouldNotUpdateScoreWhenUpdatedWithExcessiveIncrement(List<FootballMatch> matches) {
        FootballMatch match = matches.get(0);
        Team homeTeam = match.getHomeTeam();
        Team awayTeam = match.getAwayTeam();

        testScoreBoard.addMatch(homeTeam, awayTeam, null);

        assertThatThrownBy(() -> testScoreBoard.updateMatch(2, 5, homeTeam.getName(), awayTeam.getName()))
                .isInstanceOf(ScoreException.class)
                .hasMessageContaining("Score should not be greater by more than one than previous score");
    }

    @Test
    void shouldIgnoreCaseWhenFindingMatchForUpdate() {
        //A match with mixed-case names
        testScoreBoard.addMatch(new Team("England"),new Team("Portugal"), LocalDateTime.now());

        //Updating the match with different casing
        testScoreBoard.updateMatch(1,0,"ENGLAND", "portugal");

        assertEquals(1, testScoreBoard.getMatches().get(0).getScore());
    }


    @Test
    void shouldThrowExceptionWhenFinishIsCalledForInactiveMatchInScoreBoard() {
        assertThatThrownBy(() -> testScoreBoard.finishMatch("HomeTeam","AwayTeam"))
                .isInstanceOf(ScoreBoardException.class)
                .hasMessageContaining("No ongoing match found with the given teams");
    }

    @ParameterizedTest
    @MethodSource("provideMatchDataOngoingOrFinishedGame")
    void shouldFinishOngoingMatch(List<FootballMatch> matches) {
        Team homeTeam1 = matches.get(0).getHomeTeam();
        Team awayTeam1 = matches.get(0).getAwayTeam();
        Team homeTeam2 = matches.get(1).getHomeTeam();
        Team awayTeam2 = matches.get(1).getAwayTeam();
        testScoreBoard.addMatch(homeTeam1,awayTeam1, null);
        testScoreBoard.addMatch(homeTeam2,awayTeam2,null);

        // Ensure matches are active
        assertEquals(2, testScoreBoard.getMatches().size());

        // When: Finishing the match between homeTeam1 and awayTeam1
        testScoreBoard.finishMatch(homeTeam1.getName(), awayTeam1.getName());

        // Then: The match list should only contain one match (Uruguay vs Japan)
        List<Match> remainingMatches = testScoreBoard.getMatches();
        assertEquals(1, remainingMatches.size());
        assertEquals(homeTeam2.getName(), remainingMatches.get(0).getHomeTeam().getName());
    }

    @Test
    void shouldIgnoreCaseWhenFindingMatchForFinish() {
        //A match with mixed-case names
        testScoreBoard.addMatch(new Team("England"),new Team("Portugal"), LocalDateTime.now());

        //Finishing the match with different casing
        testScoreBoard.finishMatch("ENGLAND", "portugal");

        assertEquals(0, testScoreBoard.getMatches().size());
    }
}
