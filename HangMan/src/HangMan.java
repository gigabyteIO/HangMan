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
import javafx.stage.Stage;
import midi.MidiTune;

public class HangMan extends Application {

	private String userGuess;
	
	private boolean giveUp = false;
	
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
			letterButton.setOnAction( e -> doLetterButton(letterButton) );
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
		//nextWordButton.setOnAction( e -> doNextWord() );
		
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
	 * When any change is made to the state of the game that affects what should be shown in the canvas, 
	 * you just have to call draw() to completely redraw the canvas.
	 */
	public void draw() {
		
		clear();
		
		g.setFill(Color.BLACK);
		g.fillText("Guess: " + userGuess, 200, 70);
		
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
	 * When the program starts this builds the alphabet bar, main drawing canvas, and lower option bar and orients them. 
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
		
		draw();
	}
	
	/**
	 * Deals with letter presses.
	 */
	private void doLetterButton(Button button) {
		char letter = button.getText().charAt(0);
		
		for(int i = 0; i < alphabetButtons.length; i++) {
			
			if(letter == alphabetButtons[i].getText().charAt(0)){
				alphabetButtons[i].setDisable(true);
				userGuess = "" + letter;	
			}
		}
		
		draw();
	}
	
	
	
	// METHODS FOR LOWER OPTION BAR
	
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

	public static void main(String[] args) {
		launch();
	}
}
