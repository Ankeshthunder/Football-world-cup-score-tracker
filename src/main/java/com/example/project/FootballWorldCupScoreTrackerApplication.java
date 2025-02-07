package com.example.project;

import com.example.project.Interface.Match;
import com.example.project.InterfaceImpl.FootballMatch;
import com.example.project.InterfaceImpl.WorldCupScoreBoard;
import com.example.project.InterfaceImpl.WorldCupScoreBoardSummary;
import com.example.project.Model.Team;
import com.sun.tools.javac.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@SpringBootApplication
public class FootballWorldCupScoreTrackerApplication {

	private static final Logger logger = Logger.getLogger(FootballWorldCupScoreTrackerApplication.class.getName());

	public static void main(String[] args) {

		// Encapsulated team list (Immutable Copy)
		List<Team> teams = List.of(
				new Team("Mexico", 0), new Team("Canada", 5),
				new Team("Spain"), new Team("Brazil"),
    			new Team("Germany"), new Team("France"));

		LocalDateTime localDateTime = LocalDateTime.now();

		WorldCupScoreBoard worldCupScoreBoard = new WorldCupScoreBoard(new ArrayList<>());

		WorldCupScoreBoardSummary worldCupScoreBoardSummary = new WorldCupScoreBoardSummary(worldCupScoreBoard);

		// Adding three different matches and ensuring two matches start at same time with  0 - 0
		worldCupScoreBoard.addMatch(teams.get(0), teams.get(1), null);

		worldCupScoreBoard.addMatch(teams.get(2), teams.get(3), localDateTime);

		worldCupScoreBoard.addMatch(teams.get(4), teams.get(5), localDateTime);

		//Updating third match
		worldCupScoreBoard.updateMatch(1,0,teams.get(4).getName(), teams.get(5).getName());

		// finishing second match
        worldCupScoreBoard.finishMatch(teams.get(2).getName(), teams.get(3).getName());

		//fetch list of all ongoing matches
		worldCupScoreBoard.getMatches().forEach(match -> logger.info(formatMatch(match)));

		// Fetch and log sorted scoreboard summary
		worldCupScoreBoardSummary.getBoardSummaryInSortedOrderBasedOnMaximumScoredGoalsAndRecency()
				.forEach(match -> logger.info(formatMatch(match)));


		SpringApplication.run(FootballWorldCupScoreTrackerApplication.class, args);
	}

	private static String formatMatch(Match match) {
		return String.format("%s - %s (%d - %d)",
				match.getHomeTeam().getName(),
				match.getAwayTeam().getName(),
				match.getHomeTeam().getScore(),
				match.getAwayTeam().getScore());
	}

}
