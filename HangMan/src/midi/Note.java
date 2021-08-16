package midi;

/**
 * This class represents a note to be played by a SimpleSyth Object using a Midi channel.
 * @author marti
 *
 */
public class Note {

	private final int noteNumber;   // number on the scale -1 to 127 (-1 = silence)
	private final int noteDuration; // how long the note is played

	/**
	 * Note constructor. Represents a note on a scale and holds a note number and the duration of the note. 
	 * @param number the note number.
	 * @param duration the duration of the note.
	 */
	public Note(int number, int duration) {

		if ((number < -1 || number > 127) || (duration < 0 || duration > 5000))
			throw new IllegalArgumentException(
					"Note number must be from -1 to 127 and note duration should be between 0 and 5000 milliseconds");

		noteNumber = number;
		noteDuration = duration;

	}

	/**
	 * This plays the note using the synth object passed as the parameter.
	 * @param synth
	 */
	public void play(SimpleSynth synth) {

		if (noteNumber >= 0 && noteNumber <= 127) {
			synth.noteOn(noteNumber); // Turn on the i-th note in the tune.

			try {
				Thread.sleep(noteDuration); // Delay to give the note a chance to play.
			} catch (InterruptedException e) {
			}

			synth.noteOff(noteNumber); // Turn off the same note.
		}
		else if (noteNumber == -1) {
			try {
				Thread.sleep(noteDuration); // Delay to give the note a chance to play.
			} catch (InterruptedException e) {
			}
		}
		else 
			System.out.println("Not a valid note integer.");
	}

	/**
	 * Returns the note number of the note.
	 * @return noteNumber
	 */
	public int getNoteNumber() {
		return noteNumber;
	}

	/**
	 * Returns the note duration of the note. 
	 * @return noteDuration
	 */
	public int getNoteDuration() {
		return noteDuration;
	}

}
