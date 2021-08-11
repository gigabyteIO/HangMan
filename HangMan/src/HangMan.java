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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import midi.MidiTune;
import textio.TextIO;

public class HangMan extends Application {

	
	private static String word; // The word that the player is trying to guess
	private String wordSoFar;   // The word that shows any correct character guesses, otherwise shows "_ " (underscores)
	private String guesses;		// String concatenated with each character guess
	
	private boolean giveUp = false;
	private boolean newGame = true;
	private boolean userWon;
	
	private final int HEAD = 1,
					  BODY = 2,
					  LEFT_ARM = 3,
					  RIGHT_ARM = 4,
					  LEFT_LEG = 5,
					  RIGHT_LEG = 6;
	
	
	
	private GraphicsContext g;
	private Button[] alphabetButtons = new Button[26]; // Holds all the alphabet buttons
	
	
	/**
	 * Creates a pane of Alphabets that the user can click in order to make a guess in the game of HangMan.
	 * @return
	 */
	private GridPane makeAlphabetBar() {
		
		GridPane letterGridPane = new GridPane();
		
		int alphabetCounter = 0;
		for(char ch = 'A'; ch <= 'Z'; ch++) {
			String cha = "";
			
			Button letterButton;
			letterButton = new Button(cha + ch);
			alphabetButtons[alphabetCounter] = letterButton;
			letterButton.setOnAction( e -> {
				try {
					doLetterButton(letterButton);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} );
			letterButton.setPrefWidth(g.getCanvas().getWidth() / 26);
			letterButton.setPrefHeight(g.getCanvas().getHeight() / 7);
			letterGridPane.add(letterButton, alphabetCounter, 0);
			alphabetCounter++;
		}
		letterGridPane.setStyle("-fx-padding: 5px; -fx-border-color: black; -fx-border-width: 2px 0 0 0");
		
		return letterGridPane;
	}
	
	/**
	 * 
	 * @return
	 */
	private HBox makeOptionBar() {
		
		Button nextWordButton = new Button("Next Word");
		nextWordButton.setOnAction( e -> doNextWord() );
		
		Button giveUpButton = new Button("Give Up");
		giveUpButton.setOnAction( e -> doGiveUp() );
		
		Button quitButton = new Button("Quit");
		quitButton.setOnAction( e -> doQuit() );
		
		HBox optionBar = new HBox(nextWordButton, giveUpButton, quitButton);
		optionBar.setAlignment(Pos.CENTER);
		optionBar.setStyle("-fx-padding: 5px; -fx-border-color: black; -fx-border-width: 2px 0 0 0");
		
		return optionBar;
	}
	
	/**
	 * When the program starts this builds the alphabet bar, main drawing canvas, and lower option bar and orients them
	 * as well as initializes the first word being guessed and draws the canvas.
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
		
		//mainPane.setTop(makeMenuBar());
		//mainPane.setBottom(makeButtonBar());
		
		// Addes the Pane to a scene, then the scene to the stage
		Scene scene = new Scene(mainPane);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Hang Man");
		stage.show();
		
		try {
			word = randomWord("WordList.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		wordSoFar = "";
		
		for(int i = 0; i < word.length(); i++) {
			wordSoFar += " _ ";
		}
		
		guesses = "";
		
		draw();
	}
	
	/**
	 * Deals with letter presses.
	 * @throws FileNotFoundException 
	 */
	private void doLetterButton(Button button) throws FileNotFoundException {
		char letter = button.getText().charAt(0);
		
		for(int i = 0; i < alphabetButtons.length; i++) {
			
			if(letter == alphabetButtons[i].getText().charAt(0)){
				alphabetButtons[i].setDisable(true);
				hangManLogic(letter);		
			}
		}
		
		draw();
	}
	
	
	
	// METHODS FOR LOWER OPTION BAR
	/**
	 * This gets the game ready for the next word and resets the alphabet buttons
	 */
	private void doNextWord() {
		// Reset variables
		newGame = true;
		wordSoFar = "";
		guesses = "";
		
		// Pick new word
		try {
			word = randomWord("WordList.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Reset alphabet buttons
		for(int i = 0; i < alphabetButtons.length; i++) {
			
			if(alphabetButtons[i].isDisabled()){
				alphabetButtons[i].setDisable(false);
			}
		}
		
		// Resets the players guessed word
		for(int i = 0; i < word.length(); i++) {
			wordSoFar += " _ ";
		}
		
		// Redraw canvas
		draw();
	}
	
	private void doGiveUp() {
		giveUp = true;
		draw();
	}
	
	/**
	 * Exits the program when user presses the quit button.
	 */
	private void doQuit() {
		System.exit(0);
	}
	
	// *************** HANGMAN LOGIC *********************** //
	
	
	private void hangManLogic(char letter) throws FileNotFoundException {
		letter = Character.toUpperCase(letter);
		int wordLength = word.length(); // length of the randomly chosen word
		boolean correctGuess = false;

		// Check if guessed character is in any position in the word
		if(word.indexOf(letter) >= 0) {
			System.out.println("Yes, " + letter + " is in the word!\n");
		}
		else {
			System.out.println("Sorry, " + letter + " isn't in the word!\n");
		}

		// Add user guess to guesses
		guesses += letter;
		wordSoFar = "";
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
		/*
		wordSoFar = wordSoFar.replaceAll("\\s+", ""); // collapses any white space found in the word
		if(wordSoFar.equals(word)) {
			correctGuess = true;
			System.out.println("You got it!");
		}
		*/
		
		draw();
		//wordSoFar = ""; // Clear word
			
	}	 // End HangManLogic()
	
	
	// ***************** DRAWING METHODS ************************ //
	
	/**
	 * When any change is made to the state of the game that affects what should be shown in the canvas, 
	 * you just have to call draw() to completely redraw the canvas.
	 */
	public void draw() {
		// Testing git branching
		clear();
		
		// Figure out how to make text larger
		Text wordSoFar_ = new Text(wordSoFar);
		wordSoFar_.setStyle("-fx-font: 40 arial;");
		
		g.setFill(Color.BLACK);
		g.fillText("Word: " + word, 200, 70);
	
		g.fillText(wordSoFar, 45, 450, 200);
	
		
		
		//g.fillText(wordSoFar, 45, 450);
		
		if(giveUp) {
			g.setFill(Color.BLACK);
			g.fillText("Give up button has been pressed", 100, 50);
		}
	}
	
	// METHODS USED TO CLEAR AND DRAW SHAPES IN THE DRAW METHOD
	public void clear() {
		g.setFill(Color.TAN);
		g.fillRect(0, 0, 800, 600);
	}
	
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

		return word.toUpperCase();
	} // End randomWord()
	

	public static void main(String[] args) {
		launch();
	}
}
