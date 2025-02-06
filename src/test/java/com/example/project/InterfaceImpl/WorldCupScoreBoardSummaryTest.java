package com.example.project.InterfaceImpl;

import com.example.project.Interface.Match;
import com.example.project.Model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class WorldCupScoreBoardSummaryTest {

    private WorldCupScoreBoard worldCupScoreBoard;
    private WorldCupScoreBoardSummary scoreBoardSummary;

    @BeforeEach
    void setUp() {
        worldCupScoreBoard = new WorldCupScoreBoard(new ArrayList<>());
        scoreBoardSummary = new WorldCupScoreBoardSummary(worldCupScoreBoard);
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

    @ParameterizedTest
    @MethodSource("provideMatchDataOngoingOrFinishedGame")
    void shouldReturnMatchSummaryOrderedByTotalScoreAndRecency(List<FootballMatch> matches) {
        for (FootballMatch match : matches) {
            worldCupScoreBoard.addMatch(match.getHomeTeam(), match.getAwayTeam(), null);
        }
        // Get sorted matches based on total score and recency
        List<Match> sortedFootballMatches = scoreBoardSummary.getBoardSummaryInSortedOrderBasedOnMaximumScoredGoalsAndRecency();

        assertFalse(sortedFootballMatches.isEmpty());
        assertEquals(5, sortedFootballMatches.size());

        assertEquals(matches.get(3), sortedFootballMatches.get(0));  // Uruguay 6 - Italy 6
        assertEquals(matches.get(1), sortedFootballMatches.get(1));  // Spain 10 - Brazil 2
        assertEquals(matches.get(0), sortedFootballMatches.get(2));  // Mexico 0 - Canada 5
        assertEquals(matches.get(4), sortedFootballMatches.get(3));  // Argentina 3 - Australia 1
        assertEquals(matches.get(2), sortedFootballMatches.get(4));  // Germany 2 - France 2
    }

    @ParameterizedTest
    @MethodSource("provideMatchDataOngoingOrFinishedGame")
    public void shouldNotReorderMatchesWithSameTimeAndScore(List<FootballMatch> matches) {
        // Setting mock date time to ensure two different matches start at same time.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        LocalDateTime localDateTime = LocalDateTime.parse("2025-02-05T08:53:15.058499", formatter);
        worldCupScoreBoard.addMatch(matches.get(1).getHomeTeam(), matches.get(1).getAwayTeam(), localDateTime);
        worldCupScoreBoard.addMatch(matches.get(3).getHomeTeam(), matches.get(3).getAwayTeam(), localDateTime);

        List<Match> matchSummary = scoreBoardSummary.getBoardSummaryInSortedOrderBasedOnMaximumScoredGoalsAndRecency();

        assertEquals("Spain", matchSummary.get(0).getHomeTeam().getName());
        assertEquals("Uruguay", matchSummary.get(1).getHomeTeam().getName());
    }

}
