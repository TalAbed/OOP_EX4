package gameClient;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;

public class KML_Logger implements Runnable {

	private int level;
	private List<Fruit> fruits;
	private List<Robot> robots;
	private game_service game;
	private DGraph dg;
	private String KMLR="";
	private String KMLF="";
	private String KMLG="";
	private CreateObject create;
	private long timeOfGame;
	private boolean firstRun=true;
	private String start="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
			"<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" + 
			"  <Document>\r\n" ;
	private String end="</Document>\r\n" + 
			"</kml>";
	
	public void save(String kml) throws IOException {
		FileWriter fw = new FileWriter(level+".kml");  
		PrintWriter pw = new PrintWriter(fw);
		pw.println(kml);
		pw.close(); 
		fw.close();
	}
	
	private void createKMLGraph() {
		for(Iterator<node_data> ndi=dg.getV().iterator();ndi.hasNext();) {
			node_data nd=ndi.next();
			KMLG+="<Placemark>"+
					"<Style id=\"grn-blank\">\r\n" + 
					"      <IconStyle>\r\n" + 
					"        <Icon>\r\n" + 
					"          <href>http://maps.google.com/mapfiles/kml/paddle/grn-blank.png\r\n" + 
					"</href>\r\n" + 
					"        </Icon>\r\n" + 
					"      </IconStyle>\r\n" + 
					"    </Style>"+
					"<Point>\r\n" + 
					"      <coordinates>"+nd.getLocation().x()+","+nd.getLocation().y()+",0</coordinates>\r\n" + 
					"    </Point>"+
					"</Placemark>";
			try {
				for(Iterator<edge_data> edi=dg.getE(nd.getKey()).iterator();edi.hasNext();) {
					edge_data ed=edi.next();
					KMLG+="<name>polygon.kml</name>\r\n" + 
							"\r\n" + 
							"	<Style id=\"orange-5px\">\r\n" + 
							"		<LineStyle>\r\n" + 
							"			<color>ff00aaff</color>\r\n" + 
							"			<width>5</width>\r\n" + 
							"		</LineStyle>\r\n" + 
							"	</Style>\r\n" + 
							"\r\n" + 
							"\r\n" + 
							"	<Placemark>\r\n" + 
							"\r\n" + 
							"		<name>A polygon</name>\r\n" + 
							"		<styleUrl>#orange-5px</styleUrl>\r\n" + 
							"\r\n" + 
							"		<LineString>\r\n" + 
							"\r\n" + 
							"			<tessellate>1</tessellate>\r\n" + 
							"			<coordinates>\r\n" + 
							nd.getLocation().x()+","+nd.getLocation().y()+",0\r\n"+
							dg.getNode(ed.getDest()).getLocation().x()+","+dg.getNode(ed.getDest()).getLocation().y()+",0\r\n"+
							"			</coordinates>\r\n" + 
							"\r\n" + 
							"		</LineString>\r\n" + 
							"\r\n" + 
							"	</Placemark>";
				}
			}
			catch(Exception e) {}
		}

	}
	
	private void addRobotsAuto() {
		GameServer gs = CreateObject.createGameServer(game.toString());
		int countRobot = gs.getRobotsNumber();
		int countFruit = gs.getFruitsNumber();
		int i=0;
		int n;
		while(countFruit>0 && countRobot>0)
		{
			int typefruit=fruits.get(i).getType();
			if(typefruit==1)
				n = fruits.get(i).edgeFruitOn(dg).getSrc();
			else
				n = fruits.get(i).edgeFruitOn(dg).getDest();
			game.addRobot(n);
			countFruit--;
			countRobot--;
			i++;
		}
		int numOfNodes = dg.nodeSize();
		int key[] = new int[numOfNodes];
		int j = 0;
		for(Iterator<node_data> ndi = dg.getV().iterator();ndi.hasNext();) 
		{
			int point = ndi.next().getKey();
			key[j]=point;
			j++;
		}
		while(countRobot>0)	
		{
			int random = (int)(Math.random()*key.length);
			game.addRobot(key[random]);
			countRobot--;
		}
	}
	
	public void initGame() {
		game = Game_Server.getServer(level);
		dg = new DGraph();
		dg.init(game.getGraph());
		createKMLGraph();
		create = new CreateObject(game);
		fruits = create.createFruitList();
		addRobotsAuto();
		robots = create.createRobotList();
		Thread t = new Thread(this);
		t.start();
	}
	
	public void saveKmlFromGUI() {
		String kml = start + KMLG + KMLF + KMLR + end;
		try {
			save(kml);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setGraph(DGraph dg) {
		this.dg = dg;
		createKMLGraph();
	}
	
	public void addToRobots(String str) {
		KMLR = KMLR.concat(str);
	}
	public void addToFruits(String str) {
		KMLF = KMLF.concat(str);
	}
	
	@Override
	public void run() {
		game.startGame();
		AutoGame a=new AutoGame(game, fruits, robots);

		while(game.isRunning())
		{
			a.update(game, fruits, robots);
			synchronized(this) 
			{
				if(this.firstRun) {
					if(game.timeToEnd()>30000) 
						this.timeOfGame=60000;
					else
						this.timeOfGame=30000;
					this.firstRun=false;
				}
				a.moveRobots();
				create.setGame(game);
				fruits=create.createFruitList();
				robots=create.createRobotList();
				for(int i=0;i<robots.size();i++) 
					KMLR+=robotsToKml(robots.get(i),timeOfGame-game.timeToEnd());
				for(int i=0;i<fruits.size();i++) 
					KMLF+=fruitsToKml(fruits.get(i),timeOfGame-game.timeToEnd());
			}
		}
		synchronized(this) 
		{
			String kml = start + KMLG + KMLF + KMLR + end;
			try {
				save(kml);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String fruitsToKml(Fruit fruit, long l) {
		String ans;
		if(fruit.getType()==1) {
			ans="<Placemark>\r\n" + 
					"      <TimeSpan>\r\n" +
					"     <begin>"+l+"</begin>\r\n" + 
					"        <end>"+(l+1)+"</end>"+
					" </TimeSpan>\r\n" +  
					"<Style id=\"electronics\">\r\n" + 
					"      <IconStyle>\r\n" + 
					"        <Icon>\r\n" + 
					"          <href>http://maps.google.com/mapfiles/kml/shapes/electronics.png\r\n" + 
					"\r\n" + 
					"</href>\r\n" + 
					"        </Icon>\r\n" + 
					"      </IconStyle>\r\n" + 
					"    </Style>"+
					"      <Point>\r\n" + 
					"        <coordinates>"+fruit.getLocation().x()+","+fruit.getLocation().y()+",0 </coordinates>\r\n" + 
					"      </Point>\r\n" + 
					"    </Placemark>";
		}
		else {
			ans="<Placemark>\r\n" + 
					"      <TimeSpan>\r\n" +
					"     <begin>"+l+"</begin>\r\n" + 
					"        <end>"+(l+1)+"</end>"+
					" </TimeSpan>\r\n" + 
					"<Style id=\"movies\">\r\n" + 
					"      <IconStyle>\r\n" + 
					"        <Icon>\r\n" + 
					"          <href>http://maps.google.com/mapfiles/kml/shapes/movies.png\r\n" + 
					"\r\n" + 
					"</href>\r\n" + 
					"        </Icon>\r\n" +  
					"      </IconStyle>\r\n" + 
					"    </Style>"+
					"      <Point>\r\n" + 
					"        <coordinates>"+fruit.getLocation().x()+","+fruit.getLocation().y()+",0 </coordinates>\r\n" + 
					"      </Point>\r\n" + 
					"    </Placemark>";
		}
		return ans;
	}

	public String robotsToKml(Robot robot, long l) {
		String ans="<Placemark>\r\n" + 
				"      <TimeSpan>\r\n" +
				"     <begin>" + l + "</begin>\r\n" + 
				"        <end>" + (l+1)+"</end>"+
				" </TimeSpan>\r\n" + 
				"<Style id=\"lodging\">\r\n" + 
				"      <IconStyle>\r\n" + 
				"        <Icon>\r\n" + 
				"          <href>http://maps.google.com/mapfiles/kml/shapes/lodging.png\r\n" + 
				"</href>\r\n" + 
				"        </Icon>\r\n" + 
				"      </IconStyle>\r\n" + 
				"    </Style>"+
				"      <Point>\r\n" + 
				"        <coordinates>"+robot.getLocation().x()+","+robot.getLocation().y()+",0 </coordinates>\r\n" + 
				"      </Point>\r\n" + 
				"    </Placemark>";
		return ans;
	}
	
	public void setLevel (int n) {
		this.level = n;
	}
	
	public static void main(String[] args) {
		for (int i=0;i<24;i++) {
			KML_Logger kml = new KML_Logger ();
			kml.setLevel(i);
			kml.initGame();
		}
	}
	
	

}
