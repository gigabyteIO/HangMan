import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import midi.MidiTune;
import textio.TextIO;

public class HangMan extends Application {

	private static String word; // The word that the player is trying to guess
	private String wordSoFar; // The word that shows any correct character guesses, otherwise shows "_ "

	private CharacterXY[] characterCoords; // Holds the x, y coordinates for each character in the word so graphics
											// context knows where to print them.

	private String guesses; // String concatenated with each character guess
	private int wrongGuesses; // The number of guesses the player has gotten wrong
	private final int GUESS_LIMIT = 7; // The number of guesses the player gets before losing the game
	private String displayMessage;

	private boolean newGame; // Keeps track of whether it's a new game


	private GraphicsContext g; // Used for drawing on canvas
	private WordList wordList = new WordList("WordList.txt");
	private Button[] alphabetButtons = new Button[26]; // Holds all the alphabet buttons
	private MidiHangMan midi = new MidiHangMan();

	/**
	 * Creates a pane of Alphabets that the user can click in order to make a guess
	 * in the game of HangMan.
	 * 
	 * @return
	 */
	private GridPane makeAlphabetBar() {

		GridPane letterGridPane = new GridPane();

		int alphabetCounter = 0;
		for (char ch = 'A'; ch <= 'Z'; ch++) {
			String cha = "";

			Button letterButton;
			letterButton = new Button(cha + ch);
			alphabetButtons[alphabetCounter] = letterButton;
			letterButton.setOnAction(e -> {
				try {
					doLetterButton(letterButton);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			letterButton.setPrefWidth(g.getCanvas().getWidth() / 26);
			letterButton.setPrefHeight(g.getCanvas().getHeight() / 7);
			letterGridPane.add(letterButton, alphabetCounter, 0);
			alphabetCounter++;
		}
		letterGridPane.setStyle("-fx-padding: 5px; -fx-border-color: black; -fx-border-width: 2px 0 0 0");

		return letterGridPane;
	} // End makeAlphabetBar()

	/**
	 * Creates the buttons used to control the game and set's up their action
	 * listeners.
	 * 
	 * @return
	 */
	private HBox makeOptionBar() {

		Button nextWordButton = new Button("Next Word");
		nextWordButton.setOnAction(e -> doNextWord());

		// Button giveUpButton = new Button("Give Up");
		// giveUpButton.setOnAction(e -> doGiveUp());

		Button quitButton = new Button("Quit");
		quitButton.setOnAction(e -> doQuit());

		HBox optionBar = new HBox(nextWordButton, quitButton);
		optionBar.setAlignment(Pos.CENTER);
		optionBar.setStyle("-fx-padding: 5px; -fx-border-color: black; -fx-border-width: 2px 0 0 0");

		return optionBar;
	} // End makeOptionBar()

	/**
	 * When the program starts this builds the alphabet bar, main drawing canvas,
	 * and lower option bar and orients them as well as initializes the first word
	 * being guessed and draws the canvas.
	 * 
	 * @throws FileNotFoundException
	 */
	public void start(Stage stage) {
		// Creates canvas
		Canvas canvasCenter = new Canvas(800, 500);
		g = canvasCenter.getGraphicsContext2D();
		// Adds canvas to a Pane
		BorderPane mainPane = new BorderPane(canvasCenter);

		// Orients top buttons, center canvas, and lower option bar
		mainPane.setTop(makeAlphabetBar());
		mainPane.setCenter(canvasCenter);
		mainPane.setBottom(makeOptionBar());							

		// Adds the Pane to a scene, then the scene to the stage
		Scene scene = new Scene(mainPane);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Hang Man");
		stage.show();

		word = wordList.removeRandomWord();
		word = word.toUpperCase();

		// Initialize variables for first game
		newGame = true;	
		wrongGuesses = 0;
		guesses = "";
		displayMessage = "The word has " + word.length() + " letters. Let's play Hangman!\n" + "Bad Guesses Remaining: "
				+ (GUESS_LIMIT - wrongGuesses);

		wordSoFar = "";
		for (int i = 0; i < word.length(); i++) {
			wordSoFar += "  ";
		}

		characterCoords = stringToCharacterXY(word);

		// test statement
		for (int i = 0; i < word.length(); i++) {
			System.out.println("Character: " + characterCoords[i].getWord() + " X: " + characterCoords[i].getX()
					+ " Y: " + characterCoords[i].getY());
		}

		draw();
	} // End start()

	/**
	 * Deals with letter presses.
	 * 
	 * @throws FileNotFoundException
	 */
	private void doLetterButton(Button button) throws FileNotFoundException {
		char letter = button.getText().charAt(0);

		for (int i = 0; i < alphabetButtons.length; i++) {

			if (letter == alphabetButtons[i].getText().charAt(0)) {
				alphabetButtons[i].setDisable(true);
				hangManLogic(letter);
			}
		}
		draw();
	} // End doLetterButton()

	// METHODS FOR LOWER OPTION BAR
	/**
	 * This gets the game ready for the next word and resets the alphabet buttons
	 */
	private void doNextWord() {
		word = wordList.removeRandomWord();
		word = word.toUpperCase();

		int wordLength = word.length();
		// Debug statement
		System.out.println("Next word: " + word);

		// Reset variables
		newGame = true;
		wrongGuesses = 0;
		wordSoFar = "";
		guesses = "";
		displayMessage = "The word has " + wordLength + " letters. Let's play Hangman!\n" + "Bad Guesses Remaining: "
				+ (GUESS_LIMIT - wrongGuesses);

		// Reset alphabet buttons
		for (int i = 0; i < alphabetButtons.length; i++) {
			if (alphabetButtons[i].isDisabled()) {
				alphabetButtons[i].setDisable(false);
			}
		}

		// Resets the players guessed word
		for (int i = 0; i < word.length(); i++) {
			wordSoFar += "  ";
		}

		characterCoords = stringToCharacterXY(word);

		// test statement
		for (int i = 0; i < word.length(); i++) {
			System.out.println("Character: " + characterCoords[i].getWord() + " X: " + characterCoords[i].getX()
					+ " Y: " + characterCoords[i].getY());
		}

		// Redraw canvas
		draw();

	} // End doNextWord()

	/**
	 * Exits the program when user presses the quit button.
	 */
	private void doQuit() {
		System.exit(0);
	} // End doQuit()

	// *************** HANGMAN LOGIC *********************** //

	private void hangManLogic(char letter) throws FileNotFoundException {
		letter = Character.toUpperCase(letter);
		int wordLength = word.length(); // length of the randomly chosen word

		// Add user guess to guesses
		guesses += letter;
		wordSoFar = "";
		for (int i = 0; i < wordLength; i++) {
			for (int j = 0; j < guesses.length(); j++) {
				if (word.charAt(i) == guesses.charAt(j)) {
					wordSoFar = wordSoFar + "  " + guesses.charAt(j) + "  ";
					characterCoords[i].setIsGuessed();
					break;
				}
				// This took forever to figure out
				// Add a '_' to the position in wordSoFar, ONLY IF
				// there are no guesses equal to the spot in word AND it
				// has tested all the guesses
				else {
					if (j == guesses.length() - 1) {
						wordSoFar = wordSoFar + "  ";
					}
				}
			}
		}
		
		if (wrongGuesses < GUESS_LIMIT) {
			// Check if guessed character is in any position in the word
			if (word.indexOf(letter) >= 0) {
				String testWordSoFar = wordSoFar;
				testWordSoFar = testWordSoFar.replaceAll("\\s+", ""); // collapses any white space found in the word
				
				// Player won
				if (testWordSoFar.equals(word)) {
					displayMessage = "The word is complete. You win!\nClick \"Next word\" to play again";
					midi.wonTune();
				}
				// Player hasn't won, but correctly guessed a letter
				else {
					displayMessage = "Yes, " + letter + " is in the word! Pick your next letter.\n"
							+ "Bad Guesses Remaining: " + (GUESS_LIMIT - wrongGuesses);
					midi.correctGuessTune();
				}
			} else {
				wrongGuesses++; // User got the guess wrong, add one to variable
				
				// Player still has more guesses
				if (wrongGuesses != GUESS_LIMIT) {
					displayMessage = "Sorry, " + letter + " isn't in the word! Pick your next letter.\n"
							+ "Bad Guesses Remaining: " + (GUESS_LIMIT - wrongGuesses);
					midi.incorrectGuessTune();
				}
				// Deals with edge case
				// Player is out of guesses, player lost
				else { //(wrongGuesses == GUESS_LIMIT) {
					displayMessage = "Sorry, you're hung! The word is: " + word
							+ "\nClick \"Next word\" to play again.";
					midi.hungTune();
				}

			}
		}

		// draw();

	} // End HangManLogic()

	// ***************** DRAWING METHODS ************************ //

	/**
	 * When any change is made to the state of the game that affects what should be
	 * shown in the canvas, you just have to call draw() to completely redraw the
	 * canvas.
	 */
	public void draw() {

		if (newGame) {
			resetBackground();

			g.setFill(Color.RED);
			g.setFont(new Font(20));
			g.fillText(displayMessage, 20, 70);

			// resetLeftCanvas();

			// This draws the underscores showing how many characters are in the word at the
			// bottom of the canvas.
			int x1 = 5;
			int x2 = 55;
			for (int i = 0; i < word.length(); i++) {
				g.setStroke(Color.BLACK);
				g.setLineWidth(3);
				g.strokeLine(x1, 475, x2, 475);
				// Move line right a fixed amount
				x1 = x1 + 65;
				x2 = x2 + 65;
			}

			// System.out.println("Check 1 - 2");
			g.setFill(Color.BLACK);
			g.setFont(new Font(60));

			printCharacters(g, characterCoords);

			// g.fillText(wordSoFar, 50, 460, 400);

			newGame = false;
		} else {
			// This is for redrawing the display messages so they don't overlap
			resetLeftCanvas();
			resetLowerCanvas();

			int x1 = 5;
			int x2 = 55;
			for (int i = 0; i < word.length(); i++) {
				g.setStroke(Color.BLACK);
				g.setLineWidth(3);
				g.strokeLine(x1, 475, x2, 475);
				// Move line right a fixed amount
				x1 = x1 + 65;
				x2 = x2 + 65;
			}

			g.setFill(Color.RED);
			g.setFont(new Font(20));
			g.fillText(displayMessage, 20, 70);


			g.setFill(Color.BLACK);
			g.setFont(new Font(60));
			printCharacters(g, characterCoords);
	
			// Draw the hang man based on how many wrong guesses

			System.out.println("Number of wrong guesses: " + wrongGuesses);
			for (int i = 0; i < wrongGuesses; i++) {

				// HEAD
				if (wrongGuesses == 1) {
					g.setFill(Color.CORNFLOWERBLUE);
					g.fillOval(592, 100, 30, 50);
				}

				// NECK
				else if (wrongGuesses == 2) {
					g.setFill(Color.CORNFLOWERBLUE);
					g.fillRect(604, 150, 5, 35);
					// g.fillRect(579, 200, 5, 70);
				}

				// LEFT ARM
				else if (wrongGuesses == 3) {
					g.setFill(Color.CORNFLOWERBLUE);
					g.fillRect(575, 180, 30, 5);
				}

				// RIGHT ARM
				else if (wrongGuesses == 4) {
					g.setFill(Color.CORNFLOWERBLUE);
					g.fillRect(605, 180, 30, 5);
				}

				// BODY
				else if (wrongGuesses == 5) {
					g.setFill(Color.CORNFLOWERBLUE);
					g.fillRect(604, 185, 5, 35);
				}

				// LEFT LEG
				else if (wrongGuesses == 6) {
					g.setFill(Color.CORNFLOWERBLUE);
					g.fillRect(590, 220, 17, 5);
					g.fillRect(590, 220, 5, 50);

				}

				// RIGHT LEG
				else {
					g.setFill(Color.CORNFLOWERBLUE);
					g.fillRect(607, 220, 15, 5);
					g.fillRect(620, 220, 5, 50);

					// User lost display message
					// displayMessage = "Sorry, you're hung! The word is: " + word
					// + "\nClick \"Next word\" to play again.";
					resetLeftCanvas();
					// resetLowerCanvas();

					g.setFill(Color.RED);
					g.setFont(new Font(20));
					g.fillText(displayMessage, 20, 70);

				}
			}
		}
	} // End draw()

	// METHODS USED TO CLEAR AND DRAW SHAPES IN THE DRAW METHOD
	public void resetBackground() {
		g.setFill(Color.TAN);
		g.fillRect(0, 0, 800, 600);
		drawGallow();
		newGame = false;
	} // End clear()

	/**
	 * 
	 */
	private void resetLeftCanvas() {
		g.setFill(Color.TAN);
		g.fillRect(0, 0, 450, 400);
	} // End resetLeftCanvas()

	/**
	 * 
	 */
	private void resetLowerCanvas() {
		g.setFill(Color.TAN);
		g.fillRect(0, 525, 800, 75);
	} // End resetLowerCanvas()

	/**
	 * 
	 */
	private void drawGallow() {
		// Base
		g.setFill(Color.DARKGREEN);
		g.fillRect(450, 325, 300, 40);

		// Stand
		g.setFill(Color.SADDLEBROWN);
		g.fillRect(475, 25, 20, 300);

		// Branch
		g.fillRect(485, 55, 135, 10);

		// Noose
		g.fillRect(605, 65, 3, 35);
	} // End drawGallow()

	/**
	 * NEED TO FINISH THIS and TEST Converts characters in a string to X, Y
	 * coordinates stored in an array.
	 * 
	 * @param word The word being converted.
	 * @return The array of characters with their associated (X, Y) Coordinates.
	 */
	private CharacterXY[] stringToCharacterXY(String word) {
		CharacterXY[] characterCoordinates;
		characterCoordinates = new CharacterXY[word.length()];

		int x = 5;
		int y = 460;
		for (int i = 0; i < word.length(); i++) {
			char cha = word.charAt(i);
			String str = Character.toString(cha);
			characterCoordinates[i] = new CharacterXY(str, x, y);
			x = x + 66;
		}

		return characterCoordinates;
	} // End stringToCharacterXY()

	/**
	 * Prints out the characters of the word on the canvas.
	 * 
	 * @param g    The graphics context used for drawing.
	 * @param word The array that holds the characters of the word.
	 */
	private void printCharacters(GraphicsContext g, CharacterXY[] word) {
		String chr;
		int x;
		int y;
		boolean isGuessed;

		for (int i = 0; i < word.length; i++) {
			chr = word[i].getWord();
			x = word[i].getX();
			y = word[i].getY();
			isGuessed = word[i].getIsGuessed();

			if (isGuessed)
				g.fillText(chr, x, y);
		}

	} // End printCharacters()

	public static void main(String[] args) {
		launch();
	} // End main()
}
