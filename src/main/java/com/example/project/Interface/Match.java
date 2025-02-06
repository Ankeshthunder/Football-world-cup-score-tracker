package com.example.project.Interface;

import com.example.project.Model.Team;

import java.time.LocalDateTime;

public interface Match {
    int getScore();

    Team getHomeTeam();

    Team getAwayTeam();

    boolean isActive();

    LocalDateTime getStartTime();
}
