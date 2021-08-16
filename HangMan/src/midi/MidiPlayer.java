package midi;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

import midi.Note;
import midi.SimpleSynth;
import midi.Tune;

/**
 * This class represents a Midiplayer which simulates various instruments and sound effects.
 * Command line interface allows for: 
 * 1. selecting sound effect 
 * 2. changing volume 
 * 3. playing notes
 * 4. entering notes for a tune
 * 5. making computer generate a random note
 * 6. playing the tune
 * @author marti
 *
 */
public class MidiPlayer {

	private static Scanner in = new Scanner(System.in);
	private static SimpleSynth synth = new SimpleSynth();
	private static Tune tune;
	private static int userChoice;
	private static boolean isInt;

	public static void main(String[] args) {
	
		midiMenu();
		
	}

	/**
	 * Displays the interface options to interact with the Midi player.
	 * 
	 */
	public static void midiMenu() {

		while (true) {
			System.out.println("");
			System.out.println("What do you want to do?\n");
			System.out.println(" 0.  EXIT");
			System.out.println(" 1.  Select an instrument.");
			System.out.println(" 2.  Select a volume.");
			System.out.println(" 3.  Play some notes.");
			System.out.println(" 4.  Enter some notes for a tune.");
			System.out.println(" 5.  Make up a randome tune.");
			System.out.println(" 6.  Play the tune.\n");

			inputHandler(0,6);
			
			switch (userChoice) {

			case 1:
				selectInstrument();
				continue;
			case 2:
				selectVolume();
				continue;
			case 3:
				playNotes();
				continue;
			case 4:
				notesForTune();
				continue;
			case 5:
				randomTune();
				continue;
			case 6:
				playTune();
				continue;
			default:
				exit();
			}
		}
	} // end displayMenuAndHandleInput()
	
	/**
	 * Handles the user's input and make sure it is valid and between the specified range.
	 * @param lower the lower bound.
	 * @param upper the upper bound. 
	 */
	public static void inputHandler(int lower, int upper) {
		
		do {

			System.out.print("Enter the number of your choice: ");
			isInt = in.hasNextInt();

			if (isInt) {
				userChoice = in.nextInt();
				if (userChoice < lower || userChoice > upper) {
					System.out.println("-> Invalid input. Enter an integer from " + lower + " to " + upper + ".\n");
				}
			} else
				System.out.println("-> Invalid input. Character's not allowed.\n");
			in.nextLine();

		} while (!isInt || userChoice > upper || userChoice < lower);
	}

	/**
	 * Exits that program. 
	 */
	public static void exit() {
		System.out.println("-> Exiting...");
		System.exit(0);
	} // end exit()

	/**
	 * Displays all of the instruments and corresponding value and allows user to select one.
	 */
	public static void selectInstrument() {

		System.out.println("Please select an instrument number between 0 and 127 out of the list.");
		System.out.println("/*   0: */     \"Acoustic Grand Piano\",\r\n"
				+ "/*   1: */     \"Bright Acoustic Piano\",\r\n" + "/*   2: */     \"Electric Grand Piano\",\r\n"
				+ "/*   3: */     \"Honky-tonk Piano\",\r\n" + "/*   4: */     \"Electric Piano 1\",\r\n"
				+ "/*   5: */     \"Electric Piano 2\",\r\n" + "/*   6: */     \"Harpsichord\",\r\n"
				+ "/*   7: */     \"Clavi\",\r\n" + "/*   8: */     \"Celesta\",\r\n"
				+ "/*   9: */     \"Glockenspiel\",\r\n" + "/*  10: */     \"Music Box\",\r\n"
				+ "/*  11: */     \"Vibraphone\",\r\n" + "/*  12: */     \"Marimba\",\r\n"
				+ "/*  13: */     \"Xylophone\",\r\n" + "/*  14: */     \"Tubular Bells\",\r\n"
				+ "/*  15: */     \"Dulcimer\",\r\n" + "/*  16: */     \"Drawbar Organ\",\r\n"
				+ "/*  17: */     \"Percussive Organ\",\r\n" + "/*  18: */     \"Rock Organ\",\r\n"
				+ "/*  19: */     \"Church Organ\",\r\n" + "/*  20: */     \"Reed Organ\",\r\n"
				+ "/*  21: */     \"Accordion\",\r\n" + "/*  22: */     \"Harmonica\",\r\n"
				+ "/*  23: */     \"Tango Accordion\",\r\n" + "/*  24: */     \"Acoustic Guitar (nylon)\",\r\n"
				+ "/*  25: */     \"Acoustic Guitar (steel)\",\r\n" + "/*  26: */     \"Electric Guitar (jazz)\",\r\n"
				+ "/*  27: */     \"Electric Guitar (clean)\",\r\n" + "/*  28: */     \"Electric Guitar (muted)\",\r\n"
				+ "/*  29: */     \"Overdriven Guitar\",\r\n" + "/*  30: */     \"Distortion Guitar\",\r\n"
				+ "/*  31: */     \"Guitar harmonics\",\r\n" + "/*  32: */     \"Acoustic Bass\",\r\n"
				+ "/*  33: */     \"Electric Bass (finger)\",\r\n" + "/*  34: */     \"Electric Bass (pick)\",\r\n"
				+ "/*  35: */     \"Fretless Bass\",\r\n" + "/*  36: */     \"Slap Bass 1\",\r\n"
				+ "/*  37: */     \"Slap Bass 2\",\r\n" + "/*  38: */     \"Synth Bass 1\",\r\n"
				+ "/*  39: */     \"Synth Bass 2 10\",\r\n" + "/*  40: */     \"Violin\",\r\n"
				+ "/*  41: */     \"Viola\",\r\n" + "/*  42: */     \"Cello\",\r\n"
				+ "/*  43: */     \"Contrabass\",\r\n" + "/*  44: */     \"Tremolo Strings\",\r\n"
				+ "/*  45: */     \"Pizzicato Strings\",\r\n" + "/*  46: */     \"Orchestral Harp\",\r\n"
				+ "/*  47: */     \"Timpani\",\r\n" + "/*  48: */     \"String Ensemble 1\",\r\n"
				+ "/*  49: */     \"String Ensemble 2\",\r\n" + "/*  50: */     \"SynthStrings 1\",\r\n"
				+ "/*  51: */     \"SynthStrings 2\",\r\n" + "/*  52: */     \"Choir Aahs\",\r\n"
				+ "/*  53: */     \"Voice Oohs\",\r\n" + "/*  54: */     \"Synth Voice\",\r\n"
				+ "/*  55: */     \"Orchestra Hit\",\r\n" + "/*  56: */     \"Trumpet\",\r\n"
				+ "/*  57: */     \"Trombone\",\r\n" + "/*  58: */     \"Tuba\",\r\n"
				+ "/*  59: */     \"Muted Trumpet\",\r\n" + "/*  60: */     \"French Horn\",\r\n"
				+ "/*  61: */     \"Brass Section\",\r\n" + "/*  62: */     \"SynthBrass 1\",\r\n"
				+ "/*  63: */     \"SynthBrass 2\",\r\n" + "/*  64: */     \"Soprano Sax\",\r\n"
				+ "/*  65: */     \"Alto Sax\",\r\n" + "/*  66: */     \"Tenor Sax\",\r\n"
				+ "/*  67: */     \"Baritone Sax\",\r\n" + "/*  68: */     \"Oboe\",\r\n"
				+ "/*  69: */     \"English Horn\",\r\n" + "/*  70: */     \"Bassoon\",\r\n"
				+ "/*  71: */     \"Clarinet\",\r\n" + "/*  72: */     \"Piccolo\",\r\n"
				+ "/*  73: */     \"Flute\",\r\n" + "/*  74: */     \"Recorder\",\r\n"
				+ "/*  75: */     \"Pan Flute\",\r\n" + "/*  76: */     \"Blown Bottle\",\r\n"
				+ "/*  77: */     \"Shakuhachi\",\r\n" + "/*  78: */     \"Whistle\",\r\n"
				+ "/*  79: */     \"Ocarina\",\r\n" + "/*  80: */     \"Lead 1 (square)\",\r\n"
				+ "/*  81: */     \"Lead 2 (sawtooth)\",\r\n" + "/*  82: */     \"Lead 3 (calliope)\",\r\n"
				+ "/*  83: */     \"Lead 4 (chiff)\",\r\n" + "/*  84: */     \"Lead 5 (charang)\",\r\n"
				+ "/*  85: */     \"Lead 6 (voice)\",\r\n" + "/*  86: */     \"Lead 7 (fifths)\",\r\n"
				+ "/*  87: */     \"Lead 8 (bass + lead)\",\r\n" + "/*  88: */     \"Pad 1 (new age)\",\r\n"
				+ "/*  89: */     \"Pad 2 (warm)\",\r\n" + "/*  90: */     \"Pad 3 (polysynth)\",\r\n"
				+ "/*  91: */     \"Pad 4 (choir)\",\r\n" + "/*  92: */     \"Pad 5 (bowed)\",\r\n"
				+ "/*  93: */     \"Pad 6 (metallic)\",\r\n" + "/*  94: */     \"Pad 7 (halo)\",\r\n"
				+ "/*  95: */     \"Pad 8 (sweep)\",\r\n" + "/*  96: */     \"FX 1 (rain)\",\r\n"
				+ "/*  97: */     \"FX 2 (soundtrack)\",\r\n" + "/*  98: */     \"FX 3 (crystal)\",\r\n"
				+ "/*  99: */     \"FX 4 (atmosphere)\",\r\n" + "/* 100: */     \"FX 5 (brightness)\",\r\n"
				+ "/* 101: */     \"FX 6 (goblins)\",\r\n" + "/* 102: */     \"FX 7 (echoes)\",\r\n"
				+ "/* 103: */     \"FX 8 (sci-fi)\",\r\n" + "/* 104: */     \"Sitar\",\r\n"
				+ "/* 105: */     \"Banjo\",\r\n" + "/* 106: */     \"Shamisen\",\r\n" + "/* 107: */     \"Koto\",\r\n"
				+ "/* 108: */     \"Kalimba\",\r\n" + "/* 109: */     \"Bag pipe\",\r\n"
				+ "/* 110: */     \"Fiddle\",\r\n" + "/* 111: */     \"Shanai\",\r\n"
				+ "/* 112: */     \"Tinkle Bell\",\r\n" + "/* 113: */     \"Agogo\",\r\n"
				+ "/* 114: */     \"Steel Drums\",\r\n" + "/* 115: */     \"Woodblock\",\r\n"
				+ "/* 116: */     \"Taiko Drum\",\r\n" + "/* 117: */     \"Melodic Tom\",\r\n"
				+ "/* 118: */     \"Synth Drum\",\r\n" + "/* 119: */     \"Reverse Cymbal\",\r\n"
				+ "/* 120: */     \"Guitar Fret Noise\",\r\n" + "/* 121: */     \"Breath Noise\",\r\n"
				+ "/* 122: */     \"Seashore\",\r\n" + "/* 123: */     \"Bird Tweet\",\r\n"
				+ "/* 124: */     \"Telephone Ring\",\r\n" + "/* 125: */     \"Helicopter\",\r\n"
				+ "/* 126: */     \"Applause\",\r\n" + "/* 127: */     \"Gunshot\" ");
		
		System.out.println("*****INSTRUMENT SELECTION*****");
		inputHandler(0, 127);
		synth.setInstrument(userChoice);
		System.out.println("-> " + synth.getNameForInstrument(userChoice) + " has been selected.");

	} // end selectInstrument()

	/**
	 * Allows user to change the volume of the synth.
	 */
	public static void selectVolume() {
		
		System.out.println("****VOLUME SELECTION****");
		System.out.println("Enter a number from 0 (min volume) to 127 (max volume).");
		inputHandler(0,127);
		synth.setVolume(userChoice);
		System.out.println("-> Volume set to " + synth.getVolume() + ".");

	} // end selectVolume()

	/**
	 * Gets notes from the user and plays them. 
	 */
	public static void playNotes() {

		System.out.println("Enter notes between 0 and 127 and hit enter when done. (example: 0 123 45 56 34 23)");
		System.out.print("Input: ");
		String lineOfNotes = in.nextLine();
		String[] notesString = lineOfNotes.trim().split(" ");

		int[] notes = new int[notesString.length];
		for (int i = 0; i < notesString.length; i++) {
			notes[i] = Integer.parseInt(notesString[i]);
		}

		int beat = 800; // Timings are given as multiples of this many milliseconds.

		for (int j = 0; j < notes.length; j++) {
			synth.noteOn(notes[j]); // Turn on the i-th note in the tune.

			try {
				Thread.sleep(beat); // Delay to give the note a chance to play.
			} catch (InterruptedException e) {
			}

			synth.noteOff(notes[j]); // Turn off the same note.

		}
		
		System.out.println("-> Notes played.");

	} // end playNotes()

	/**
	 * Gets notes from the user and creates a tune from them. 
	 */
	public static void notesForTune() {
		tune = new Tune();
		System.out.println("Enter notes between 0 and 127 and hit enter when done. (example: 0 123 45 56 34 23)");
		System.out.print("Input: ");
		String lineOfNotes = in.nextLine(); // gets full line of user input
		String[] notesString = lineOfNotes.trim().split(" "); // splits it up based on blank spaces and puts it an array of strings
		
		for (int i = 0; i < notesString.length; i++) {
			int noteNumber = Integer.parseInt(notesString[i]);	 // extracts the note numbers from the string array
			tune.add(new Note(noteNumber, 1000));	// adds a new note to the tune object
		}
		
		System.out.println("-> Tune created.");

	} // end notesForTune()

	/**
	 * Creates a random tune of notes. Notes range from 36 to 84.
	 * Large changes in successive notes are rarer than small changes.
	 */
	public static void randomTune() {
		tune = new Tune();
		Random ran = new Random();
		int noteNumber;
		
		for(int i = 0; i < 30; i++) {
			
			double stratifiedRandom = Math.random();
			
			// Notes will range from 36 to 84
			
			// Most common, make small changes to note
			if(stratifiedRandom >= 0 && stratifiedRandom <= .5) {
				noteNumber = ran.nextInt(56) + 8; // random number between (56 and 64)
				tune.add(new Note(noteNumber, 1000));
				//System.out.println("56 to 64");
			}
			
			// Second most common, slightly larger changes to note
			else if(stratifiedRandom > .5 && stratifiedRandom <= .75) {
				noteNumber = ran.nextInt(51) + 18; // random number between (51 and 69)
				tune.add(new Note(noteNumber, 1000));
				//System.out.println("51 to 69");
			}
			
			// Third most common, slightly larger changes to note
			else if(stratifiedRandom > .75 && stratifiedRandom <= .90) {
				noteNumber = ran.nextInt(46) + 28; // random number between (46 and 74)
				tune.add(new Note(noteNumber, 1000));
				//System.out.println("46 to 74");
			}
			
			// Least common, this is between .91 - .99, this is for large changes to the note
			// whole range(36 - 84)
			else {
				noteNumber = ran.nextInt(36) + 48; // random number between (36 and 84)
				tune.add(new Note(noteNumber, 1000));
				//System.out.println("36 to 84");
			}		
		}
		
		System.out.println("-> Random tune created.");
		
	} // end randomTune()

	/**
	 * Play's a tune. 
	 */
	public static void playTune() {
		if(tune == null) 
			System.out.println("-> A tune does not exist yet. Either create one or select option 5 to have the computer create one.");
		else
			tune.play(synth);
	} // end playTune()
}
