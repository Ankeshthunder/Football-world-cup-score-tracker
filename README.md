
This project implements a simple library to track the live scores of the World Cup football matches. The solution focuses on simplicity, adhering to good software design principles such as Clean Code and SOLID principles. 
It’s built using an in-memory data structure (e.g., collections in Java) to store the information related to the scores of each match.

Project Requirements

    Live Scoreboard: The library should allow tracking the score of ongoing football matches in real-time.
    In-memory Store: Data is stored in memory using simple collections (e.g., dictionaries, lists) rather than a database or external storage solution.
    Test-Driven Development (TDD): The implementation follows TDD principles. Unit tests are used to verify the correctness of the functionality.
    Object-Oriented Design: The code adheres to Object-Oriented principles, ensuring maintainable, extendable, and clean code.
    SOLID Principles: The design follows SOLID principles to make the code modular and easily testable.

Features

    Track scores of football matches in real-time.
    Update match status (ongoing or finished) and the current score of ongoing matches.
    Handle edge cases (e.g., match score resets, invalid inputs).
    Provide a simple library to interact with the scoreboard.

Code Quality:

    - All testcases follows AAA pattern (Arrange, Act, Assert) to ensure break down of test into three distinct sections, 
        each focusing on a specific part of the test's execution that is 
        "what is being prepared", "what action is being taken", and "what is expected".
    - The code follows SOLID principles and is organized in an object-oriented manner.
    - Test-Driven Development (TDD): Unit tests are written before actual solution to ensure modular, maintainable, and well-structured code with
       around 100% line and test coverage.
    -Clean and well-documented code with proper error handling.

Guidelines : 

    In Order to run test cases, it is required to install
       - Java 21 
       - Maven 3.6.3
    steps to run : 
       1. Clone this repository
       2. mvn clean test

Requirements:

    The scoreboard supports the following operations:
        1. Start a new game, assuming initial score 0 – 0 and adding it the scoreboard.
            This should capture following parameters:
                a. Home team
                b. Away team
        2. Update score. This should receive a pair of absolute scores: home team score and away
           team score.
        3. Finish game currently in progress. This removes a match from the scoreboard.
        4. Get a summary of games in progress ordered by their total score. The games with the same
           total score will be returned ordered by the most recently started match in the scoreboard.
           For example, if following matches are started in the specified order and their scores
           respectively updated:

           a. Mexico 0 - Canada 5
           b. Spain 10 - Brazil 2
           c. Germany 2 - France 2
           d. Uruguay 6 - Italy 6
           e. Argentina 3 - Australia 1

           The summary should be as follows:
           1. Uruguay 6 - Italy 6
           2. Spain 10 - Brazil 2
           3. Mexico 0 - Canada 5
           4. Argentina 3 - Australia 1
           5. Germany 2 - France 2

Assumptions :
      
     1. The user needs to keep all started matches to use the objects for updating and finishing them.
     2. When any match get started, score for both team must be initiated with 0-0 and register time to check recency for summary board.
     3. Using paramitrized tests to ensure ordering of matches and testing multiple match fields.
     4. we can't update score with negative value, excessive increment(like more than 1 than previous) and with decreased value of exisitng score.
     5. All the test cases are following AAA pattern which means first arrange data, then act on method and then assert result.
     6. Two different matches can be start on same time.
     7. No two match can exist between same home and away team.
     8. Scoreboard Summary will be sorted by score (descending), start time (descending), and maintain original order of insertion if both are the same.
  
Authors:

    Ankesh kumar (aankeshpandey3623@gmail.com)


