package com.timpanix.hangmangame;
import java.util.Random;
import java.util.Scanner;


public class Hangman {

	// static variables
	private static final String[] BODY_PARTS_LEVEL_1 = {" ", " (_)", " (_)", "\\(_)", "\\(_)/", "\\(_)/", "\\(_)/"};
	private static final String[] BODY_PARTS_LEVEL_2 = {" ", " ",    "  |",  " \\|",  " \\|/",  " \\|/",  " \\|/"};
	private static final String[] BODY_PARTS_LEVEL_3 = {" ", " ",    "  |",   "  |",   "  |",    "  |",    "  |",};
	private static final String[] BODY_PARTS_LEVEL_4 = {" ", " ",    "  |",   "  |",   "  |",    "  |",    "  |",};
	private static final String[] BODY_PARTS_LEVEL_5 = {" ", " ",    " ",     " ",     " ",      " /",     " / \\"};
	private static final String[] BODY_PARTS_LEVEL_6 = {" ", " ",    " ",     " ",     " ",      "/",      "/   \\"};
	private static final String[] STORED_WORDS = {"rhythm", "pizza", "pyjama", "jazz", "yoghurt", "engineer", 
										  "difference", "something", "computer", "percussion", "brain"};
	private static final int MAX_LETTERS = 10;
	private static final int NUM_OF_HANGING_LEVELS = 6;
	private static int winCounter = 0;
	private static int lossCounter = 0;
	private static boolean[] usedStoredWords = new boolean[getStoredWords().length];
	// instance variables
	private char[] alphabet;
	private char[] usedCharsHits;
	private char[] usedCharsMisses;
	private String secretWord;
	private char[] maskedSecretWord;
	private boolean[] status;
	private int numOfHits;
	private int numOfMisses;
	
	// constructor
	public Hangman(){

		setAlphabet();
		usedCharsMisses = new char[getNumOfHangingLevels()]; // this char[] will store all the chars that the player guessed incorrectly
		numOfHits = 0;
		numOfMisses = 0;
	}
		
	public static int getNumOfHangingLevels(){
		return NUM_OF_HANGING_LEVELS;
	}
	
	public static String[] getStoredWords(){
		return STORED_WORDS;
	}
	
	public static int getMaxLetters(){
		return MAX_LETTERS;
	}
	
	public static boolean[] getUsedStoredWords(){
		return usedStoredWords;
	}
		
	public void setSecretWord(String secretWord){
		this.secretWord = secretWord;
		
		// invoke method to set the masked version of the secret word to the right amount of underscores
		setMaskedSecretWord(secretWord.length());
		
		// instantiate a char array to store all the successfully guessed letters
		usedCharsHits = new char[secretWord.length()];
		
		// instantiate new boolean array. All elements are set to default value of false.
		// this array will be used to keep track of which letters of the secret word have been uncovered
		// eg. if the 2nd and 4th letter of the word have been uncovered, the 2nd and 4th element of this boolean
		// array would be "true", the other elements would be "false"
		status = new boolean[secretWord.length()];
		for(int i = 0; i < secretWord.length(); i++)
			status[i] = false;
	}
	
	private void setAlphabet(){
		
		alphabet = new char[26];
		char c = 97;	// ascii value of 97 is "a"
		
		for(int i = 0; i < 26; i++){
			alphabet[i] = c;	// this fills the alphabet array with a-z
			c++;
		}
	}
	
	public char[] getAlphabet(){
		return this.alphabet;
	}
	
	public void updateAlphabet(char c){
		
		for(int j = 0; j < 26; j++){
			if(getAlphabet()[j] == c){
				alphabet[j] = ' ';	// replace the matched letter with empty space
				break; 	// break the loop as soon as you found the match
			}
		}
	}
	
	public void updateStats(boolean won){
		
		if(won)
			winCounter++;
		else
			lossCounter++;
	}
	
	public static void displayStats(){
		System.out.println("Statistics:");
		System.out.println("Wins:\t" + winCounter);
		System.out.println("Losses:\t" + lossCounter);
	}
	
	public String getSecretWord(){
		return this.secretWord;
	}
			
	public void setMaskedSecretWord(int length){
		
		this.maskedSecretWord = new char[length];
		for(int i = 0; i < length; i++)
			maskedSecretWord[i] = '*';
	}
	
	public void displayTitle() {
		
		System.out.println("     ____                                     ____");
		System.out.println("     |/ |                                     | \\|");	// the \ needs to be escaped
		System.out.println("     | ()       H A N G M A N   G A M E       () |");
		System.out.println("     |          -----------------------          |");
		System.out.println("     |                                           |\n\n");
	}

	/**
	 * this method displays a graphical image of the hangman
	 */
	public void displayHangman(int i){

		System.out.println("  __________");
		System.out.println("  |  / 	   |");
		System.out.println("  | /      |");
		System.out.println("  |/     " + BODY_PARTS_LEVEL_1[i]);
		System.out.println("  |      " + BODY_PARTS_LEVEL_2[i]);
		System.out.println("  |      " + BODY_PARTS_LEVEL_3[i]);
		System.out.println("  |      " + BODY_PARTS_LEVEL_4[i]);
		System.out.println("  |   	 " + BODY_PARTS_LEVEL_5[i]);
		System.out.println("  |      " + BODY_PARTS_LEVEL_6[i]);
		System.out.println("  |");
		System.out.println(" _|_\n");
	}

	public int newOrStoredSecretWord(Scanner sc) {
		
		String option = new String();
		
		displayLine();
		System.out.println("Would you like to guess a stored secret word or input a new one?");
		System.out.println(" S\ta stored secret word");
		System.out.println(" N\ta new one");
		System.out.print("Your selection: (S/N) ");
		
		option = sc.next().toUpperCase();	// get the user input and convert to upper case (in case player uses lowercase)
		while (!(option.equals("S") || option.equals("N"))){	// loop until the user inputs either S or N
			System.out.print("Invalid input. Please try again: ");
			option = sc.next().toUpperCase();
		}
		// if the player choses to guess a stored word, return 1; otherwise 2
		// if the player choses to input a new word, return 2
		return (option.equals("S")) ? 1 : 2;
	}

	public int howManyPlayers(Scanner sc) {

		System.out.println("How many players are involved in this game?");
		System.out.println(" 1\tI'm playing alone");
		System.out.println(" 2\t2 or more players\n");
		System.out.print("Your selection: (1/2) ");
		return sc.nextInt();
	}

	public void setSecretWordFromStorage() {

		Random r = new Random();
		int i = r.nextInt(getStoredWords().length - 1);	// get a random value between 0 and n ( n = no. of elements in words array - 1) 
		while(getUsedStoredWords()[i])	// loop until a random value is produced which doesn't represent an already used stored word
			i = r.nextInt(getStoredWords().length - 1);
		setSecretWord(getStoredWords()[i]);		// set the secret word
		updateUsedStoredWords(i);		// and set the responding flag to true so this word won't be available for future random selections with the same player
	}

	private void updateUsedStoredWords(int i) {
		usedStoredWords[i] = true;
	}

	public void setSecretWordFromPlayer(Scanner sc) {

		String input = new String();
		int flag = -1;
		
		System.out.print("Please input your secret word: ");
		while(flag != 0){
			input = sc.next().trim().toLowerCase();	// remove white space and convert to lowercase
			flag = checkInput(input);
			if(flag == 1)
				System.out.print("Your word is too long (max. 10 characters). Please chose another one: ");
			else if(flag == 2)
				System.out.print("This is not a word. Please input a word (max. 10 characters): ");
		}
		setSecretWord(input);
	}


	/*
	 *  this method checks if a word (the secret word input by the player)
	 *  - consists of only letters
	 *  - is between 2 and 10 characters long
	 */
	private int checkInput(String input) {
		
		if(input.matches("[a-z]{2," + getMaxLetters() + "}"))
			return 0;	// everything is fine: input was a word (between 2 and 10 characters long)
		else if(input.length() > getMaxLetters())
			return 1;	// input is longer than 10 letters --> return 1
		else
			return 2;	// input consists not only of letters --> return 2
	}

	public int makeGuess(String input) {
			
		char[] guess = input.toCharArray();	// convert the input String to a char[]
		boolean matchFound = false;
		
	// invalid input
		if(! input.matches("[a-z]"))
			return -1; 	// input was not a letter or more than one letter
		else if(alreadyUsed(guess[0]))
			return -2;	// this letter has already been used
	// valid input
		else{		// input was a letter that has not been used yet
			for(int i = 0; i < getSecretWord().length(); i++){	// loop through the secret word and check if there is a match	
				
				// if a match has been found
				if(getSecretWord().charAt(i) == guess[0]){ 
					maskedSecretWord[i] = guess[0];				// unmask the successfully guessed letter and
					status[i] = true;							// switch the value of the relevant boolean element to true
					// if the guessed letter is found in the secret word for the first time
					if(!matchFound){
						usedCharsHits[numOfHits++] = guess[0];	// update the char[] of the successful guesses
						updateAlphabet(guess[0]); 				// update the alphabet array (replace the valid guess with space)
						matchFound = true;						// switch the flag to true
					}
				} 
				// if the last element is reached and at least 1 match was found (could be the last element itself)
				if((i == getSecretWord().length() - 1) && matchFound)
					return 1;	// success!
			}
			// if the input was valid but the guess unsuccessful
			// update the char[]of the unsuccessful guesses
			usedCharsMisses[numOfMisses++] = guess[0];	// store the unsuccessful guess in the char[]
			
			// update the alphabet array
			updateAlphabet(guess[0]);
			
			return 2;		// valid but no success
		}
	}

	private boolean alreadyUsed(char c) {
		
		for(int i = 0; i < usedCharsHits.length; i++)
			if(c == usedCharsHits[i])	// if the char is already in the hits list...
				return true;			// return true (= already used)
		
		for(int i = 0; i < usedCharsMisses.length; i++)
			if(c == usedCharsMisses[i])	// if the char is already in the misses list...
				return true;			// return true (= already used)
				
		return false;	// otherwise return false
	}
	
	public void displayLetterLists(){
		
		// display hits:
		System.out.print("\nHits:\t   ");
		for(int i = 0; i < numOfHits; i++){
			if(i == numOfHits - 1)	// if last value: print no comma after but a newline instead
				System.out.print(usedCharsHits[numOfHits - 1]);
			else
				System.out.print(usedCharsHits[i] + ", ");
		}
		
		// display misses:
		System.out.print("\nMisses:\t   ");
		for(int i = 0; i < numOfMisses; i++){
			if(i == numOfMisses - 1)	// if last value: print no comma after but a newline instead
				System.out.print(usedCharsMisses[numOfMisses - 1]);
			else
				System.out.print(usedCharsMisses[i] + ", ");
		}
		
		// display remaining letters in the alphabet
		int numOfRemLetters = alphabet.length - numOfHits - numOfMisses;
		int counter = 0;	// counts the remaining letters
		System.out.print("\nRemaining: ");
		// loop through alphabet array
		for(int i = 0; i < alphabet.length; i++){
			// if not empty, then print it out
			if(alphabet[i] != ' '){
				if(counter == numOfRemLetters - 1)	// if last remaining letter: print no comma but newline instead
					System.out.print(alphabet[i] + "\n");
				else{
					System.out.print(alphabet[i] + ", ");
					counter++;
				}
			}
		}
	}

	public boolean checkIfUncovered() {
		
		for(int i = 0; i < getSecretWord().length(); i++){
			if(status[i] == false)	// if status[i] is false this means that the letter in secretWord[i] is still uncovered
				return false;	// game is not over yet
		}
		// if the whole array was looped through successfully because all elements are set to true
		return true;	 // game over because the whole word is uncovered
	}
	
	public void displayLine() {
		System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - -");
	}

	public void displayDoubleLine(){
		System.out.println("=======================================================");
	}
	
	public void displayMaskedSecretWord() {
		//System.out.println("The secret word is: " + maskedSecretWord.toString());	
		System.out.print("The secret word is: ");
		for(char c : maskedSecretWord)
			System.out.print(c);
	}
	
	public void displayGameNumber(int numOfGame){

		System.out.println("");
		displayDoubleLine();
		System.out.println("\tS T A R T   O F   G A M E   N O .  " + numOfGame);
		displayDoubleLine();
	}
	

	public boolean playAnotherGame(Scanner sc) {
		
		System.out.print("Would you like to play again? (Y/N) ");
		String input = sc.next().toUpperCase();
		// loop until valid input
		while(!(input.equals("Y") || input.equals("N"))){
			System.out.println("This is not a valid option. Please try again: (Y/N) ");
			input = sc.next().toUpperCase();
		}
		if(input.equals("Y"))
			return true;
		else
			return false;
	}
}