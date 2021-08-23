

public class CharacterXY {
	private String word;
	private boolean isGuessed;
	private int x;
	private int y;
	
	public CharacterXY(String word, int x, int y) {
		this.word = word;
		this.x = x;
		this.y = y;
		this.isGuessed = false;
	}
	
	public String getWord() {
		return word;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean getIsGuessed() {
		return isGuessed;
	}
	
	public void setIsGuessed() {
		this.isGuessed = true;
	}
	
	

}
