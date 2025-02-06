package com.example.project.InterfaceImpl;

import com.example.project.Interface.Match;
import com.example.project.Interface.ScoreBoardSummary;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WorldCupScoreBoardSummary implements ScoreBoardSummary {

    private final WorldCupScoreBoard worldCupScoreBoard;

    public WorldCupScoreBoardSummary(WorldCupScoreBoard worldCupScoreBoard) {
        this.worldCupScoreBoard = worldCupScoreBoard;
    }

    @Override
    public List<Match> getBoardSummaryInSortedOrderBasedOnMaximumScoredGoalsAndRecency() {
        // Get All the ongoing matches
        List<Match> footballMatches = worldCupScoreBoard.getMatches();
        // Sort matches by score (descending), start time (descending), and maintain original order if both are the same
        return footballMatches.stream()
                // Sort by score in descending order
                .sorted(Comparator.comparingInt(Match::getScore).reversed()
                        // If the scores are the same, sort by start time in descending order
                        .thenComparing(Match::getStartTime, Comparator.reverseOrder())
                        // If both score and time are the same, maintain original order using the index in the list
                        .thenComparingInt(footballMatches::indexOf))
                .collect(Collectors.toList());
    }
}
