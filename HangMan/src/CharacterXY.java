
/**
 * Holds information for drawing characters of a word on the canvas used for HangMan.
 * @author marti
 *
 */
public class CharacterXY {
	private String word; 		// The character represented as a string
	private boolean isGuessed;	// Has the character been guessed yet
	private int x;				// X-coordinate where the character should be drawn
	private int y;				// Y-coordinate where the character should be drawn
	
	/**
	 * CharacterXY constructor. 
	 * @param word The characters(or full word)
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 */
	public CharacterXY(String word, int x, int y) {
		this.word = word;
		this.x = x;
		this.y = y;
		this.isGuessed = false;
	}
	
	/**
	 * Returns the charactor.
	 * @return the word.
	 */
	public String getWord() {
		return word;
	}
	
	/**
	 * Returns x-coordinate.
	 * @return x-coordinate.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Returns y-coordinate.
	 * @return y-coordinate.
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Returns whether the character has been guessed yet.
	 * @return isGuessed
	 */
	public boolean getIsGuessed() {
		return isGuessed;
	}
	
	/**
	 * Sets the value of isGuessed to true. Should only be called if the user guesses the character. 
	 */
	public void setIsGuessed() {
		this.isGuessed = true;
	}
	
	

}
