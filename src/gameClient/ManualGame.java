package gameClient;

import java.util.Iterator;
import java.util.List;

import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.node_data;
import utils.StdDraw;

public class ManualGame {
	
	private game_service game;
	private DGraph dg;
	private Graph_Algo ga;
	private List<Robot> robots;
	private int destNode = -1;
	private int destByMouse;
	private Robot robotToMove;
	
	public ManualGame (game_service game, List<Robot> robots) {
		this.game = game;
		this.robots = robots;
		this.dg = new DGraph();
		this.dg.init(this.game.getGraph());
		this.ga = new Graph_Algo();
		this.ga.init(dg);
	}
	
	public void update (game_service game, List<Robot> robots) {
		this.game = game;
		this.robots = robots;
	}
	
	public void moveRobots() {
		List <String> l = game.move();
		if (l!=null) {
			long timeLeft = game.timeToEnd();
			if (StdDraw.isMousePressed()) {
				double x = StdDraw.mouseX();
				double y = StdDraw.mouseY();
				this.destNode = findNode (x,y);
			}
			if (this.destNode !=-1) {
				double min = Double.POSITIVE_INFINITY;
				int i = 0;
				for (int j=0;j<l.size();j++) {
					int src = robots.get(j).getSrc();
					if (ga.shortestPathDist(src, destNode)<min) {
						min = ga.shortestPathDist(src, destNode);
						robotToMove = robots.get(j);
						i = j;
					}
				}
				if (ga.shortestPath(robotToMove.getSrc(), destNode).size()==1)
					this.destByMouse = destNode;
				else
					this.destByMouse = ga.shortestPath(robotToMove.getSrc(), destNode).get(i).getKey();
				game.chooseNextEdge(robotToMove.getId(), this.destByMouse);
				System.out.println("Robot " + robotToMove.getId() + " is going to node " + destByMouse + ", time left : " + (timeLeft/1000));
			}
		}
	}

	private int findNode(double x, double y) {
		double min = Double.POSITIVE_INFINITY;
		int src = 0;
		Iterator <node_data> ndi = dg.getV().iterator();
		while (ndi.hasNext()) {
			int n = ndi.next().getKey();
			if (Math.abs(x-dg.getNode(n).getLocation().x()) + Math.abs(y-dg.getNode(n).getLocation().y())<min) {
				min = Math.abs(x-dg.getNode(n).getLocation().x()) + Math.abs(y-dg.getNode(n).getLocation().y());
				src = n;
			}
		}
		return src;
	}

}
