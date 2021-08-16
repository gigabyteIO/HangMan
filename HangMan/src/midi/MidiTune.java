package midi;

import java.util.Random;

/**
 * Creates a random tune using a SimpleSynth.
 * 
 * @author marti
 *
 */
public class MidiTune {

	private static SimpleSynth synth = new SimpleSynth();
	private static Tune tune;

	/**
	 * MidiTune constructor.
	 */
	public MidiTune() {
		synth.setInstrument(0); // grand piano
		randomTune();

	}

	/**
	 * Creates a random tune of notes. Notes range from 36 to 84. Large changes in
	 * successive notes are rarer than small changes.
	 */
	private static void randomTune() {
		tune = new Tune();
		Random ran = new Random();
		int noteNumber;

		for (int i = 0; i < 100; i++) {

			double stratifiedRandom = Math.random();

			// Notes will range from 36 to 84

			// Most common, make small changes to note
			if (stratifiedRandom >= 0 && stratifiedRandom <= .5) {
				noteNumber = ran.nextInt(56) + 8; // random number between (56 and 64)
				tune.add(new Note(noteNumber, 1000));
				// System.out.println("56 to 64");
			}

			// Second most common, slightly larger changes to note
			else if (stratifiedRandom > .5 && stratifiedRandom <= .75) {
				noteNumber = ran.nextInt(51) + 18; // random number between (51 and 69)
				tune.add(new Note(noteNumber, 1000));
				// System.out.println("51 to 69");
			}

			// Third most common, slightly larger changes to note
			else if (stratifiedRandom > .75 && stratifiedRandom <= .90) {
				noteNumber = ran.nextInt(46) + 28; // random number between (46 and 74)
				tune.add(new Note(noteNumber, 1000));
				// System.out.println("46 to 74");
			}

			// Least common, this is between .91 - .99, this is for large changes to the
			// note
			// whole range(36 - 84)
			else {
				noteNumber = ran.nextInt(36) + 48; // random number between (36 and 84)
				tune.add(new Note(noteNumber, 1000));
				// System.out.println("36 to 84");
			}
		}

		System.out.println("-> Random tune created.");

	} // end randomTune()

	/**
	 * Play's a tune.
	 */
	public static void playTune() {
		if (tune == null)
			System.out.println(
					"-> A tune does not exist yet. Either create one or select option 5 to have the computer create one.");
		else
			tune.play(synth);
	} // end playTune()
}
