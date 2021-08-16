package midi;

import java.util.ArrayList;

/**
 * This class represents a tune which is a collection of one or more notes linked together,
 * and played in succession.
 * @author marti
 *
 */
public class Tune {
	
	private ArrayList<Note> noteList; // holds the notes for the tune
	
	/**
	 * Tune constructor. Represents a list of notes played one after the other.
	 */
	public Tune() {
		noteList = new ArrayList<Note>();
	}
	
	
	/**
	 * Adds a note to the tune.
	 * @param note the note.
	 */
	public void add(Note note) {
		noteList.add(note);
	}
	
	/**
	 * Returns the size of the tune. (e.g. how many notes are in the tune)
	 * @return
	 */
	public int getSize() {
		return noteList.size();
	}
	
	/**
	 * Plays the tune.
	 * @param synth Synth object. 
	 */
	public void play(SimpleSynth synth) {
		
		for(int i = 0; i < noteList.size(); i++) {
			noteList.get(i).play(synth);
		}
	}

}
