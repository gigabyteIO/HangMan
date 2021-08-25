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

	/**
	 * MidiTune constructor.
	 */
	public MidiHangMan() {
		synth.setInstrument(0); // grand piano
	}
	
	/**
	 * Plays one organ note when player makes a wrong guess.
	 */
	public void incorrectGuessTune() {
		tune = new Tune();
		
		// Organ
		synth.setInstrument(19);
		
		int low = 40;
		int high = 60;
		
		int noteNumber = ran.nextInt(high - low) + low;
		//System.out.println("Note #: " + noteNumber);
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
		
		int low = 40;
		int high = 60;
		
		int noteNumber = ran.nextInt(high - low) + low;
		//System.out.println("Note #: " + noteNumber);
		tune.add(new Note(noteNumber, 500));
		noteNumber = ran.nextInt(high - low) + low;
		tune.add(new Note(noteNumber, 500));
		noteNumber = ran.nextInt(high - low) + low;
		tune.add(new Note(noteNumber, 2000));
		
		tune.play(synth);	
	} // End hungTune()
	
	/**
	 * Plays a bird chirp when player makes a correct guess.
	 */
	public void correctGuessTune() {
		tune = new Tune();
		// Ohs
		synth.setInstrument(53);
		
		int low = 80;
		int high = 100;
		
		int noteNumber = ran.nextInt(high - low) + low;
		//System.out.println("Note #: " + noteNumber);
		tune.add(new Note(noteNumber, 1000));
		
		tune.play(synth);
	} // End correctGuessTune()
	
	/**
	 * Plays applause when player guesses the word correctly.
	 */
	public void wonTune() {
		tune = new Tune();
		
		// Ohs
		synth.setInstrument(53);
		int low = 80;
		int high = 100;
		
		int noteNumber = ran.nextInt(high - low) + low;
		//System.out.println("Note #: " + noteNumber);
		tune.add(new Note(noteNumber, 1000));
		noteNumber = ran.nextInt(high - low) + low;
		tune.add(new Note(noteNumber, 1000));
		noteNumber = ran.nextInt(high - low) + low;
		tune.add(new Note(noteNumber, 2000));
		
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
