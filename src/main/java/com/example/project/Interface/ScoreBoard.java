package com.example.project.Interface;

import com.example.project.Model.Team;

import java.time.LocalDateTime;
import java.util.List;

public interface ScoreBoard {
    void addMatch(Team homeTeam, Team awayTeam, LocalDateTime localDateTime);

    List<Match> getMatches();

    void updateMatch(int homeTeamScore, int awayTeamScore,  String homeTeamName, String awayTeamName);

    void finishMatch(String homeTeamName, String awayTeamName);


}

