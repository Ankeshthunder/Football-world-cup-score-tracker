package com.example.project.InterfaceImpl;

import com.example.project.Interface.Match;
import com.example.project.Model.Team;

import java.time.LocalDateTime;
import java.util.Objects;

public class FootballMatch implements Match {
    private final Team homeTeam;
    private final Team awayTeam;
    private final LocalDateTime startTime;
    private final boolean isActive;

    public FootballMatch(Team homeTeam, Team awayTeam) {
        this(homeTeam, awayTeam, LocalDateTime.now()); // Calls the other constructor with the current time
    }

    // Constructor with explicit startTime
    public FootballMatch(Team homeTeam, Team awayTeam, LocalDateTime startTime) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.isActive = true;
        this.startTime = startTime;
    }

    @Override
    public Team getHomeTeam() {
        return homeTeam;
    }

    @Override
    public Team getAwayTeam() {
        return awayTeam;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public int getScore() {
        return homeTeam.getScore() + awayTeam.getScore();
    }

    @Override
    public LocalDateTime getStartTime() {
       return startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FootballMatch footballMatch = (FootballMatch) o;
        return Objects.equals(homeTeam.getName(), footballMatch.homeTeam.getName())
                && Objects.equals(awayTeam.getName(), footballMatch.awayTeam.getName());
    }

}
