package gameClient;

public class SavingScore {
	
	private int level;
	private double score; 
	private int moves;
	public SavingScore(int level, double score, int moves) {
		super();
		this.level = level;
		this.score = score;
		this.moves = moves;
	}
	public int getLevel() {
		return level;
	}
	public double getScore() {
		return score;
	}
	public int getMoves() {
		return moves;
	}
	
	public String toStringBestScore() {
		return "the best score for level " + this.level + " is " + this.score;
	}

}
