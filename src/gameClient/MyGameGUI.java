package gameClient;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.StdDraw;

public class MyGameGUI implements Runnable {

	private game_service game;
	private DGraph dg;
	private double maxX=Double.NEGATIVE_INFINITY;
	private double maxY=Double.NEGATIVE_INFINITY;
	private double minX=Double.POSITIVE_INFINITY;
	private double minY=Double.POSITIVE_INFINITY;
	private String typegame;
	private int level;
	private static int counter=0;
	private List<Fruit> fruits;
	private List<Robot> robots;
	private CreateObject create;
	private String saveAsKml;
	private KML_Logger kml;
	private boolean firstRun =true;
	private long GameTime;
	private int id;
	
	public MyGameGUI(game_service game, int level, String typegame, DGraph dg, String iskml,int id) {
		this.id=id;
		StdDraw.enableDoubleBuffering();
		this.game=game;
		System.out.println(game.getRobots());
		this.level=level;
		this.typegame=typegame;
		this.dg=dg;
		this.saveAsKml=iskml;
		if(saveAsKml.equals("yes")) {
			kml = new KML_Logger();
			kml.setGraph(dg);
			kml.setLevel(level);
		}
		porpor();
		paint();
		create=new CreateObject(game);
		fruits=create.createFruitList(); 
		drawFruits();
		StdDraw.show();
		robots=create.createRobotList();
		if(typegame.equals("Automatic"))
			addRobotAuto();
		else
			addRobotByMouse();
		//robots=create.createRobotList();
		drawRobots();
		Thread t=new Thread(this);
		t.start();
	}
	

	private void draw()
	{
		StdDraw.clear();
		paint();
		create.setGame(game);
		fruits=create.createFruitList();
		drawFruits();
		robots=create.createRobotList();
		drawRobots();
		StdDraw.show();
	}
	
	private void drawRobots() {
		counter++;
		for (int i=0; i<robots.size(); i++) {
			Robot r = robots.get(i);
			StdDraw.picture(r.getLocation().x(), r.getLocation().y(),"robot.jpg", 0.001, 0.001);
		}
	}

	private void addRobotByMouse() {
		GameServer gameSer=CreateObject.createGameServer(game.toString());
		int countRobot=gameSer.getRobotsNumber();
		int numOfNodes = dg.nodeSize();
		Object key[]=new Object[numOfNodes];
		int j=0;
		for(Iterator<node_data> ndi=dg.getV().iterator();ndi.hasNext();) 
		{
			int point = ndi.next().getKey();
			key[j] = point;
			j++;
		}
		for(int i=1;i<=countRobot;i++) 
		{
			int v=(Integer)JOptionPane.showInputDialog(null,"choose a node to put robot "+i,"Add robot",JOptionPane.QUESTION_MESSAGE,null,key,null);
			game.addRobot(v);
			StdDraw.setPenColor(Color.MAGENTA);
			StdDraw.setPenRadius(0.025);
			StdDraw.point(dg.getNode(v).getLocation().x(),dg.getNode(v).getLocation().y());
		}
	}

	private void addRobotAuto() {
		GameServer gameSer=CreateObject.createGameServer(game.toString());
		int countRobot=gameSer.getRobotsNumber();
		int countFruit=gameSer.getFruitsNumber();
		int i = 0;
		int n;
		while(countFruit>0 && countRobot>0)
		{
			if(fruits.get(i).getType()==1)
				n = fruits.get(i).edgeFruitOn(dg).getSrc();
			else
				n = fruits.get(i).edgeFruitOn(dg).getDest();
			game.addRobot(n);
			countFruit--;
			countRobot--;
			i++;
		}
		int numOfNodes = dg.nodeSize();
		int key[]=new int[numOfNodes];
		int j=0;
		for(Iterator<node_data> ndi = dg.getV().iterator();ndi.hasNext();) {
			int point=ndi.next().getKey();
			key[j]=point;j++;
		}
		while(countRobot>0)	{
			int random=(int)(Math.random()*key.length);
			game.addRobot(key[random]);
			countRobot--;
		}
	}

	private void drawFruits() {
		for(int i=0;i<fruits.size();i++) {
			Fruit f=fruits.get(i);
			if(f.getType()==1)
				StdDraw.picture(f.getLocation().x(), f.getLocation().y(),"apple.jpg", 0.0005, 0.0005);
			else
				StdDraw.picture(f.getLocation().x(), f.getLocation().y(),"banana.jpg", 0.0005, 0.0005);
		}
	}

	private void paint() {
		for(Iterator<node_data> ndi = dg.getV().iterator();ndi.hasNext();) //paint all the the vertices in the graph
		{
			int point=ndi.next().getKey();
			StdDraw.setPenColor(Color.RED);
			StdDraw.setPenRadius(0.020);
			StdDraw.point(dg.getNode(point).getLocation().x(),dg.getNode(point).getLocation().y());
			StdDraw.text(dg.getNode(point).getLocation().x(),dg.getNode(point).getLocation().y()+0.00025, (""+point));

			try {
				for(Iterator<edge_data> edi=dg.getE(point).iterator();edi.hasNext();) 
				{
					edge_data line=edi.next();
					int dest=dg.getNode(line.getDest()).getKey();
					int src=point;
					StdDraw.setPenColor(Color.BLUE);
					StdDraw.setPenRadius(0.005);
					StdDraw.line(dg.getNode(src).getLocation().x(), dg.getNode(src).getLocation().y(), dg.getNode(dest).getLocation().x(), dg.getNode(dest).getLocation().y());
				}
			}
			catch (Exception e) {}
		}
		long time=game.timeToEnd();
		GameServer result=CreateObject.createGameServer(game.toString());
		StdDraw.setPenColor(Color.BLACK);
		StdDraw.setPenRadius(0.020);
		StdDraw.text(minX+(maxX-minX)*0.85,minY+(maxY-minY)*0.95, "The time is:"+time/1000);//paint the time on the window GUI
		StdDraw.text(minX+(maxX-minX)*0.15,minY+(maxY-minY)*0.95, "Level:"+level);//paint the level on the window GUI
		StdDraw.text(minX+(maxX-minX)*0.50,minY+(maxY-minY)*0.95, "grade:"+result.getGrade());
	}

	private void porpor() {
		for(Iterator<node_data> ndi=dg.getV().iterator();ndi.hasNext();)
		{
			int point = ndi.next().getKey();
			if(dg.getNode(point).getLocation().x()>maxX)
				maxX=dg.getNode(point).getLocation().x();
			if(dg.getNode(point).getLocation().y()>maxY)
				maxY=dg.getNode(point).getLocation().y();
			if(dg.getNode(point).getLocation().x()<minX)
				minX=dg.getNode(point).getLocation().x();
			if(dg.getNode(point).getLocation().y()<minY)
				minY=dg.getNode(point).getLocation().y();	
		} 
		double epsilon=0.0025;
		StdDraw.setCanvasSize(600,600);
		minX=minX-epsilon;maxX=maxX+epsilon;
		minY=minY-epsilon/2;maxY=epsilon/2+maxY;
		StdDraw.setXscale(minX,maxX);
		StdDraw.setYscale(minY,maxY);
	}
	
	private void outPut()
	{
		String results = game.toString();
		if(this.saveAsKml.equals("yes")) 
		{
			kml.saveKmlFromGUI();
			game.sendKML(level+".kml");
		}
		System.out.println("Game Over: "+results);

		Object result[]=new Object[2];
		result[0]="yes";
		result[1]="no";
		String seeDB=(String)JOptionPane.showInputDialog(null,"Do you want to see your DB score?","DB score", JOptionPane.QUESTION_MESSAGE,null,result,null);
		if(seeDB.equals("yes")) {
			SimpleDB sdb=new SimpleDB();
			String str=sdb.StringForGUI(level, id,1);
			JOptionPane.showMessageDialog(null, str);
		}
		seeDB=(String)JOptionPane.showInputDialog(null,"Do you want to see your DB score compare to the class?","DB score", JOptionPane.QUESTION_MESSAGE,null,result,null);
		if(seeDB.equals("yes")) {
			SimpleDB sdb=new SimpleDB();
			String str=sdb.StringForGUI(level, id,2);
			JOptionPane.showMessageDialog(null, str);
		}
	}

	@Override
	public void run() {
		game.startGame();
		AutoGame ag=new AutoGame(game, fruits, robots);
		ManualGame mg=new ManualGame(game, robots);

		while(game.isRunning())
		{
			synchronized(this) 
			{
				if(this.saveAsKml.equals("yes")) {
					if(this.firstRun) {
						if(game.timeToEnd()>30000) 
							this.GameTime=60000;
						else
							this.GameTime=30000;
						this.firstRun=false;
					}
				}
				if(typegame=="Automatic")
				{
					ag.update(game, fruits, robots);
					ag.moveRobots();
				}
				else	
				{
					mg.update(game, robots);
					mg.moveRobots();
				}
			}
			draw();
			if(this.saveAsKml.equals("yes")) {
				for(int i=0;i<robots.size();i++) 
					kml.addToRobots(kml.robotsToKml(robots.get(i),(GameTime-game.timeToEnd())/1000));
				for(int i=0;i<fruits.size();i++) 
					kml.addToFruits(kml.fruitsToKml(fruits.get(i),(GameTime-game.timeToEnd())/1000));
			}
			try
			{
				Thread.sleep(100);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}		
		outPut();

	}
	
	public static void main(String[] args) {
		game_service gs = Game_Server.getServer(0);
		DGraph dg = new DGraph();
        MyGameGUI game = new MyGameGUI(gs, 7, "Automatic", dg, "No", 322525957);
    }
}



