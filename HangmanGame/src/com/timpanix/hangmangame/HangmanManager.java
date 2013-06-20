package com.timpanix.hangmangame;
import java.util.Scanner;



public class HangmanManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		int noOfPlayers = 0;
		int wordOption = 1;	// default value assumes 1 player
		int gameCounter = 0;
		boolean playAgain = true;
		boolean firstGame = true;
		
		while(playAgain){
			// instantiate a new game
			Hangman game = new Hangman();
		
			// if first game: display the title and ask how many players are taking part
			if(firstGame){
				game.displayTitle();
				noOfPlayers = game.howManyPlayers(sc);
			}
		
			
			// if more than one player are participating, ask the players if they would like to type in the secret word themselves
			if(noOfPlayers > 1){
				wordOption = game.newOrStoredSecretWord(sc);	// if the players decide to use their own word, the value of wordOption changes to 2
			}
				
			// chose secret word depending on players choice
			if(wordOption == 1){		// player(s) guess a stored secret word
				game.setSecretWordFromStorage();
				//storedWordCounter++;
			}
			else if(wordOption == 2)	// players input their own secret word
				game.setSecretWordFromPlayer(sc);
		
			// - - - - - - - start of the game - - - - - - - - - 
			boolean uncovered = false;
			int hangingLevel = 0;
			int numOfRound = 1;
			int flag = 0;
			
			// display "START OF GAME NO. ?? "
			game.displayGameNumber(++gameCounter);
		
			// play until the secret word has been uncovered or the man has been "hanged"
			while(!uncovered && hangingLevel < Hangman.getNumOfHangingLevels()){
				System.out.println("Round " + numOfRound + ": ");
				game.displayHangman(hangingLevel);	// display hangman image
				game.displayMaskedSecretWord();		// display "The secret word is: ******* etc."
				if(numOfRound != 1)
					game.displayLetterLists();	// don't display the letter lists in the first round
				System.out.print("\nYour guess: ");
				flag = game.makeGuess(sc.next());
				// invalid input:
				while(flag < 1){	// if the value returned indicates invalid input, loop until an unused char is input
					if(flag == -1)
						System.out.print("Invalid input. Please input ONE letter only: ");
					if(flag == -2)
						System.out.print("This letter has already been used. Please try again: ");
					flag = game.makeGuess(sc.next());
				}
			
				// valid input:
				if(flag == 1){	// successful guess
					System.out.println("\t--> Success! You guessed correctly.");
					uncovered = game.checkIfUncovered();
				}
				if(flag == 2){	// unsuccessful guess
					System.out.println("\t--> Oh, no! Your guess was unsuccessful.");
					hangingLevel++;
				}
				if(! uncovered){
					game.displayLine();
					numOfRound++;
				}
			}
			
			// game is finished
			game.displayDoubleLine();
			if(uncovered){
				System.out.println("Congratulations! You've escaped the hangman successfully!");
				game.updateStats(true);	// true signals that the game was won
			}else{
				System.out.println(" G A M E   O V E R !");
				game.displayHangman(hangingLevel);
				game.updateStats(false);	// false signals that the game was lost
			}
			System.out.println("\nThe secret word was " + game.getSecretWord() + ".");
			game.displayDoubleLine();	// display double line
			// ask player(s) if he/she/they would like to play again
			playAgain = game.playAnotherGame(sc);
			if(playAgain)
				firstGame = false;	// if yes, set boolean firstGame to false so the no. of players won't be asked again
		}
		Hangman.displayStats();
		System.out.println("\n= = = = = = = = = =  T H E   E N D  = = = = = = = = = =");
		sc.close();
	}	// end main

} // end of class HangmanManager