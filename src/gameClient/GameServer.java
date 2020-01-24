package gameClient;

public class GameServer {
	
	private int fruitsNumber;
	private int moves;
	private int grade;
	private int robotsNumber;
	private String graph;
	
	
	
	public GameServer(int fruitsNumber, int moves, int grade, int robotsNumber, String graph) {
		super();
		this.fruitsNumber = fruitsNumber;
		this.moves = moves;
		this.grade = grade;
		this.robotsNumber = robotsNumber;
		this.graph = graph;
	}

	public int getFruitsNumber() {
		return fruitsNumber;
	}
	
	public int getMoves() {
		return moves;
	}
	
	public int getGrade() {
		return grade;
	}
	
	public int getRobotsNumber() {
		return robotsNumber;
	}
	
	public String getGraph() {
		return graph;
	}
	
}
