import java.util.Random;

import midi.Note;
import midi.SimpleSynth;
import midi.Tune;

/**
 * Creates and plays tunes for events in HangMan.
 * The tunes are for 1) correct guesses
 * 					 2) incorrect guesses
 * 					 3) winning the game
					 4) losing the game
 * @author marti
 *
 */
public class MidiHangMan {

	private SimpleSynth synth = new SimpleSynth();
	private Tune tune;
	private Random ran = new Random();
	// hungNotes = (120, 121, 122, 123, 124);

	/**
	 * MidiTune constructor.
	 */
	public MidiHangMan() {
		synth.setInstrument(0); // grand piano
	}

	/**
	 * Plays a bird chirp when player makes a correct guess.
	 */
	public void correctGuessTune() {
		tune = new Tune();
		// Bird chirp
		synth.setInstrument(123);
		
		int noteNumber = ran.nextInt(127);
		tune.add(new Note(noteNumber, 1000));
		
		tune.play(synth);
	} // End correctGuessTune()
	
	/**
	 * Plays one organ note when player makes a wrong guess.
	 */
	public void incorrectGuessTune() {
		tune = new Tune();
		
		// Organ
		synth.setInstrument(19);
		
		int noteNumber = ran.nextInt(127);
		tune.add(new Note(noteNumber, 1000));
		
		tune.play(synth);
	} // End incorrectGuessTune()
	
	/**
	 * Plays organ when player runs out of guesses.
	 */
	public void hungTune() {
		tune = new Tune();
		
		// Organ
		synth.setInstrument(19);
		
		int noteNumber = ran.nextInt(127);
		tune.add(new Note(noteNumber, 1000));
		noteNumber = ran.nextInt(127);
		tune.add(new Note(noteNumber, 1000));
		noteNumber = ran.nextInt(127);
		tune.add(new Note(noteNumber, 1000));
		
		tune.play(synth);	
	} // End hungTune()
	
	/**
	 * Plays applause when player guesses the word correctly.
	 */
	public void wonTune() {
		tune = new Tune();
		
		// Applause
		synth.setInstrument(126);
		
		int noteNumber = ran.nextInt(127);
		tune.add(new Note(noteNumber, 1000));
		noteNumber = ran.nextInt(127);
		tune.add(new Note(noteNumber, 1000));
		noteNumber = ran.nextInt(127);
		tune.add(new Note(noteNumber, 1000));
		
		tune.play(synth);
	} // End wonTune()

	/**
	 * Play's a tune.
	 */
	public void playTune() {
		if (tune == null)
			System.out.println(
					"-> A tune does not exist yet. Either create one or select option 5 to have the computer create one.");
		else
			tune.play(synth);
	} // end playTune()
}
