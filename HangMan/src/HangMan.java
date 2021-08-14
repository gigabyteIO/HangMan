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
								// (underscores)
	private String guesses; // String concatenated with each character guess
	private int wrongGuesses; // The number of guesses the player has gotten wrong
	private final int GUESS_LIMIT = 7; // The number of guesses the player gets before losing the game
	private String displayMessage;

	private boolean giveUp; // Variable that tracks whether player has pressed the "give up" button
	private boolean newGame; // Keeps track of whether it's a new game
	private boolean playerWon, playerLost;

	private GraphicsContext g; // Used for drawing on canvas
	private Button[] alphabetButtons = new Button[26]; // Holds all the alphabet buttons

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

		// Picks a word from the word list.
		try {
			word = randomWord("WordList.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Initialize variables for first game
		giveUp = false;
		newGame = true;
		playerWon = false;
		wrongGuesses = 0;
		guesses = "";
		displayMessage = "The word has " + word.length() + " letters. Let's play Hangman!\n" + "Bad Guesses Remaining: "
				+ (GUESS_LIMIT - wrongGuesses);

		wordSoFar = "";
		for (int i = 0; i < word.length(); i++) {
			wordSoFar += " _ ";
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
		// Pick new word
		try {
			word = randomWord("WordList.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int wordLength = word.length();

		// Reset variables
		newGame = true;
		// playerWon = false;
		// playerLost = false;
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
			wordSoFar += " _ ";
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

		// Check if guessed character is in any position in the word
		if (word.indexOf(letter) >= 0) {
			displayMessage = "Yes, " + letter + " is in the word! Pick your next letter.\n" + "Bad Guesses Remaining: "
					+ (GUESS_LIMIT - wrongGuesses);
		} else if (word.indexOf(letter) == -1) {
			wrongGuesses++; // User got the guess wrong, add one to variable
			displayMessage = "Sorry, " + letter + " isn't in the word! Pick your next letter.\n"
					+ "Bad Guesses Remaining: " + (GUESS_LIMIT - wrongGuesses);
		}

		// Add user guess to guesses
		guesses += letter;
		wordSoFar = "";
		for (int i = 0; i < wordLength; i++) {
			for (int j = 0; j < guesses.length(); j++) {
				if (word.charAt(i) == guesses.charAt(j)) {
					wordSoFar = wordSoFar + " " + guesses.charAt(j) + " ";
					break;
				}
				// This took forever to figure out
				// Add a '_' to the position in wordSoFar, ONLY IF
				// there are no guesses equal to the spot in word AND it
				// has tested all the guesses
				else {
					if (j == guesses.length() - 1) {
						wordSoFar = wordSoFar + " _ ";
					}
				}
			}
		}
		// Tests loop condition, if the user has guessed the word it ends the loop
		String testWordSoFar = wordSoFar;

		testWordSoFar = testWordSoFar.replaceAll("\\s+", ""); // collapses any white space found in the word
		if (testWordSoFar.equals(word)) {
			playerWon = true;
			displayMessage = "The word is complete. You win!\nClick \"Next word\" to play again";

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
			
			g.setFill(Color.BLACK);
			g.setFont(new Font(60));
			g.fillText(wordSoFar, 50, 350, 200);
		} else {
			// This is for redrawing the display messages so they don't overlap
			resetLeftCanvas();

			g.setFill(Color.RED);
			g.setFont(new Font(20));
			g.fillText(displayMessage, 20, 70);

			// Figure out how to make text larger
			// Text wordSoFar_ = new Text(wordSoFar);
			// wordSoFar_.setStyle("-fx-font: 40 arial;");
			g.setFill(Color.BLACK);
			g.setFont(new Font(60));
			g.fillText(wordSoFar, 50, 350, 200);

			// if(wrongGuesses < GUESS_LIMIT) {
			// Draw the hang man based on how many wrong guesses

			System.out.println("Number of wrong guesses: " + wrongGuesses);
			for (int i = 0; i < wrongGuesses; i++) {

				// HEAD
				if (wrongGuesses == 1) {
					g.setFill(Color.CORNFLOWERBLUE);
					g.fillOval(592, 175, 30, 50);
				}

				// NECK
				else if (wrongGuesses == 2) {
					g.setFill(Color.CORNFLOWERBLUE);
					g.fillRect(604, 225, 5, 35);
					// g.fillRect(579, 200, 5, 70);
				}

				// LEFT ARM
				else if (wrongGuesses == 3) {
					g.setFill(Color.CORNFLOWERBLUE);
					g.fillRect(575, 255, 30, 5);
				}

				// RIGHT ARM
				else if (wrongGuesses == 4) {
					g.setFill(Color.CORNFLOWERBLUE);
					g.fillRect(605, 255, 30, 5);
				}

				// BODY
				else if (wrongGuesses == 5) {
					g.setFill(Color.CORNFLOWERBLUE);
					g.fillRect(604, 260, 5, 35);
				}

				// LEFT LEG
				else if (wrongGuesses == 6) {
					g.setFill(Color.CORNFLOWERBLUE);
					g.fillRect(590, 295, 17, 5);
					g.fillRect(590, 295, 5, 50);

				}

				// RIGHT LEG
				else if (wrongGuesses == 7) {
					g.setFill(Color.CORNFLOWERBLUE);
					g.fillRect(607, 295, 15, 5);
					g.fillRect(620, 295, 5, 50);

					// User lost display message
					displayMessage = "Sorry, you're hung! The word is: " + word
							+ "\nClick \"Next word\" to play again.";
					resetLeftCanvas();

					g.setFill(Color.RED);
					g.setFont(new Font(20));
					g.fillText(displayMessage, 20, 70);

					g.setFill(Color.BLACK);
					g.setFont(new Font(60));
					g.fillText(wordSoFar, 50, 350, 200);

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

	private void resetLeftCanvas() {
		g.setFill(Color.TAN);
		g.fillRect(0, 0, 450, 600);
	}

	private void drawGallow() {
		// Base
		g.setFill(Color.DARKGREEN);
		g.fillRect(425, 400, 300, 40);

		// Stand
		g.setFill(Color.SADDLEBROWN);
		g.fillRect(475, 100, 20, 300);

		// Branch
		g.fillRect(485, 130, 135, 10);

		// Noose
		g.fillRect(605, 140, 3, 35);
	}

	/**
	 * Picks a random word from a list from a text file specified by the user.
	 * 
	 * @param wordList this should be the file name that contains list of words(each
	 *                 word should be on a new line).
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
		while (sc.hasNext()) {
			numberOfWords++;
			sc.next();
		}

		sc = new Scanner(new File(wordList));
		int rand = (int) (numberOfWords * Math.random()); // Computes a random number between 0 and size of list
		numberOfWords = 0;
		while (sc.hasNext()) {
			numberOfWords++;
			// Assigns the string word to randomly chosen string
			if (rand == numberOfWords) {
				word = sc.nextLine();
				break;
			}
			sc.nextLine(); // process next data
		}
		sc.close(); // close file

		return word.toUpperCase();
	} // End randomWord()

	public static void main(String[] args) {
		launch();
	} // End main()
}
