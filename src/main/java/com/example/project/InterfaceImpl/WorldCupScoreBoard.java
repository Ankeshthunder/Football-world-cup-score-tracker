package com.example.project.InterfaceImpl;

import com.example.project.Exception.ScoreBoardException;
import com.example.project.Interface.Match;
import com.example.project.Interface.ScoreBoard;
import com.example.project.Model.Team;

import java.time.LocalDateTime;
import java.util.List;

public class WorldCupScoreBoard implements ScoreBoard {

    //define list of football matches as private to ensure encapsulation
    private final List<Match> footballMatches;

    public WorldCupScoreBoard(List<Match> footballMatches) {
        this.footballMatches = footballMatches;
    }

    @Override
    public void addMatch(Team homeTeam, Team awayTeam, LocalDateTime localDateTime) {

        FootballMatch footballMatch = new FootballMatch(homeTeam, awayTeam, (localDateTime != null) ? localDateTime : LocalDateTime.now());

        // Check if the match already exists in the list and prevent duplicates
        if (footballMatches.contains(footballMatch)) {
            throw new ScoreBoardException(
                    "Oops! The match has already started"
            );
        }
        footballMatches.add(footballMatch);
    }

    @Override
    public List<Match> getMatches() {
        return footballMatches;
    }

    @Override
    public void finishMatch(String homeTeamName, String awayTeamName) {
        Match activeMatch = footballMatches.stream()
                .filter(match -> match.isActive()
                        && match.getHomeTeam().getName().equalsIgnoreCase(homeTeamName)
                        && match.getAwayTeam().getName().equalsIgnoreCase(awayTeamName))
                .findFirst()
                .orElseThrow(() -> new ScoreBoardException("No ongoing match found with the given teams"));

        // Mark the match as finished and remove it from the list
        footballMatches.remove(activeMatch);
    }

    @Override
    public void updateMatch(int homeTeamScore, int awayTeamScore, String homeTeamName, String awayTeamName) {
        Match matchToUpdate = footballMatches.stream()
                .filter(match -> match.isActive()
                        && match.getHomeTeam().getName().equalsIgnoreCase(homeTeamName)
                        && match.getAwayTeam().getName().equalsIgnoreCase(awayTeamName))
                .findFirst()
                .orElseThrow(() -> new ScoreBoardException("There is no ongoing match to update score"));

        matchToUpdate.getHomeTeam().setScore(homeTeamScore);
        matchToUpdate.getAwayTeam().setScore(awayTeamScore);
    }


}
