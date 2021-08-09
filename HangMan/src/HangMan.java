import javafx.application.Application;
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
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import midi.MidiTune;

public class HangMan extends Application {

	private GraphicsContext g,gLower;
	//private MidiTune music = new MidiTune();
	
	//private static Thread musicThread;
	
	private char selected;
	
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
		
		return letterGridPane;
	}
	
	public void start(Stage stage) {
		Canvas canvasCenter = new Canvas(800, 500);
		Canvas canvasLower = new Canvas(800, 200);
		g = canvasCenter.getGraphicsContext2D();
		gLower = canvasLower.getGraphicsContext2D();
		BorderPane mainPane = new BorderPane(canvasCenter);
		//mainPane.setTop(canvasUpper);
		mainPane.setTop(makeAlphabetBar());
		mainPane.setCenter(canvasCenter);
		mainPane.setBottom(canvasLower);
		
		//mainPane.setTop(makeMenuBar());
		//mainPane.setBottom(makeButtonBar());
		Scene scene = new Scene(mainPane);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Hang Man");
		stage.show();

		//canvas.setOnMousePressed(e -> doMouseDown(e.getX(), e.getY()));
		//canvas.setOnMouseDragged(e -> doMouseDrag(e.getX(), e.getY()));
		
		g.setFill(Color.TAN);
		g.fillRect(0, 0, 800, 600);
		
		
		//gLower.setFill(Color.RED);
		//gLower.fillRect(0, 0, 200, 200);
	
	}
	
	/**
	 * Deals with letter presses.
	 */
	private void doLetterButton(Button button) {
		char letter = button.getText().charAt(0);
		
		for(int i = 0; i < alphabetButtons.length; i++) {
			
			if(letter == alphabetButtons[i].getText().charAt(0)){
				alphabetButtons[i].setDisable(true);
			}
		}
		
	}

	public static void main(String[] args) {
		launch();
	}
}
