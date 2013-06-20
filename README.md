HangmanGame
===========

This is my version of the well known Hangman game. Dies ist meine Version des allgemein bekannten Spiels "Galgenmännchen".

The program consists of two classes:

- Hangman: the actual game

- HangmanManager: the main method of the program.

The program allows a player to play more than one game without shutting down. When the program starts, it asks how many players are taking part in the game. While hangman is a one-person game, I thought it could be funny, if one player could input a secret word for another player and vice-versa. 

If the number of players involved is greater than 1, the program asks the players, if they would like to guess one of the stored secret words or input a new word. If there is only 1 player involved, this option is not displayed and one of the stored secret words is randomly selected.

After every guess, the program displays a simple graphical image of the hanging man's current state. It also displays an updated list with the correctly guessed letters, the misses and the remaining letters in the alphabet.

At the end of the game, the player is asked if he/she would like to play again. If the player decides to quit, the amount of wins and losses are displayed.

