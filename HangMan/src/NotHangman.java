import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import textio.TextIO;

public class NotHangman {

	public static void main(String[] args) throws FileNotFoundException {

		notHangMan();

	}


	/**
	 * 
	 * @throws FileNotFoundException
	 */
	public static void notHangMan() throws FileNotFoundException {
		String fileName; // file name of word list
		String word, guesses, wordSoFar, wordFlag; // randomly chosen word the user makes guesses against
		boolean correctGuess;
		char userLetter; // character that user inputs
		int wordLength; // length of the randomly chosen word

		fileName = "WordList.txt";
		word = randomWord(fileName);
		wordSoFar = "";
		wordFlag = "";
		guesses = "";
		wordLength = word.length();
		correctGuess = false;

		// Builds underscored word
		//for(int i = 0; i < wordLength; i++) {
		//tempWord += " _ ";
		//}

		System.out.println(word);
		//System.out.println(wordLength);
		//System.out.println("The word so far:" + tempWord + "\n");

		// Print out initial setup
		System.out.println("I am thinking of a word.");
		System.out.println("Try to guess the letters that occur in the word.\n");
		System.out.print("The word so far: ");
		for(int i = 0; i < wordLength; i++) {
			System.out.print(" _ ");
		}
		System.out.println("");

		while(!correctGuess) {
			System.out.print("Enter your next letter: ");
			userLetter = TextIO.getlnChar();
			userLetter = Character.toLowerCase(userLetter); // change input to lowercase

			if(word.indexOf(userLetter) >= 0) {
				System.out.println("Yes, " + userLetter + " is in the word!\n");
			}
			else {
				System.out.println("Sorry, " + userLetter + " isn't in the word!\n");
			}

			// Add user guess to guesses
			guesses += userLetter;

			for(int i = 0; i < wordLength; i++) {
				for(int j = 0; j < guesses.length(); j++) {
					if(word.charAt(i) == guesses.charAt(j)) {
						wordSoFar = wordSoFar + " " + guesses.charAt(j) + " ";
						break;
					}
					// This took forever to figure out
					// Add a '_' to the position in wordSoFar, ONLY IF 
					// there are no guesses equal to the spot in word AND it 
					// has tested all the guesses
					else {
						if(j == guesses.length() - 1) {
							wordSoFar = wordSoFar + " _ ";
						}
					}
				}
			}

			System.out.println("Guesses so far: " + guesses + " \n");
			System.out.println("The word so far: " + wordSoFar);

			// Tests loop condition, if the user has guessed the word it ends the loop
			wordSoFar = wordSoFar.replaceAll("\\s+", ""); // collapses any white space found in the word
			if(wordSoFar.equals(word)) {
				correctGuess = true;
				System.out.println("You got it!");
			}
			
			wordSoFar = ""; // Clear word
		}
	} // End notHangMan()


	/**
	 * Picks a random word from a list from a text file specified by the user.
	 * @param wordList this should be the file name that contains list of words(each word should be on a new line).
	 * @return a random word from the list.
	 * @throws FileNotFoundException 
	 */
	public static String randomWord(String wordList) throws FileNotFoundException {
		File fileName = new File(wordList);
		Scanner sc = new Scanner(fileName);

		String word = null;
		int numberOfWords;

		numberOfWords = 0;
		// Checks how many words are in the list
		while(sc.hasNext()){
			numberOfWords++;
			sc.next();
		}

		sc = new Scanner(new File(wordList));
		int rand = (int) (numberOfWords * Math.random()); // Computes a random number between 0 and size of list
		numberOfWords = 0;
		while(sc.hasNext()) {
			numberOfWords++;
			// Assigns the string word to randomly chosen string
			if(rand == numberOfWords) {
				word = sc.nextLine();
				break; 
			}
			sc.nextLine(); // process next data
		}
		sc.close(); // close file

		return word;
	} // End randomWord()
}
