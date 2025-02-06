package com.example.project.Model;


import com.example.project.Exception.ScoreException;

public class Team {

    private final String name;

    private int score;

    public Team(String name) {
        this.name = name;
        this.score = 0;
    }

    public Team(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        if (score < 0) {
            throw new ScoreException("Score should not be negative");
        }

        if (score < this.score) {
            throw new ScoreException("Score should not be less than previous one");
        }

        if ((score - this.score) > 1) {
            throw new ScoreException("Score should not be greater by more than one than previous score");
        }

        this.score = score;
    }
}
